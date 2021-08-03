package com.infinumacademy.project

import com.infinumacademy.project.dtos.CarDto
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.CarSerialNumberAlreadyExistsException
import com.infinumacademy.project.exceptions.WrongCarDataException
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import com.infinumacademy.project.services.CarModelService
import com.infinumacademy.project.services.CarService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDate

class CarServiceTest {

    private val carRepository = mockk<CarRepository>()
    private val carCheckUpRepository = mockk<CarCheckUpRepository>()
    private val carModelService = mockk<CarModelService>()

    private lateinit var carService: CarService

    @BeforeEach
    fun setUp() {
        carService = CarService(carRepository, carCheckUpRepository, carModelService)
    }

    @Test
    fun test1() {
        val car = TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }.copy(id = 1)
        every { carRepository.findById(1) } returns car
        every { carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(1) } returns listOf(
            TestData.carCheckUpToAdd1.toCarCheckUp { car },
            TestData.carCheckUpToAdd2.toCarCheckUp { car }
        )
        assertThat(carService.getCarWithId(1)).isEqualTo(
            CarDto(car, listOf(
                TestData.carCheckUpToAdd1.toCarCheckUp { car },
                TestData.carCheckUpToAdd2.toCarCheckUp { car }
            ))
        )
        every { carRepository.findById(2) } returns null
        assertThatThrownBy {
            carService.getCarWithId(2)
        }.isInstanceOf(CarNotFoundException::class.java).hasMessage("404 NOT_FOUND \"Car with id 2 not found\"")
        verify(exactly = 2) { carRepository.findById(any()) }
        verify(exactly = 1) { carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(any()) }
    }

    @Test
    fun test2() {
        assertThatThrownBy {
            carService.addCar(TestData.carToAdd1.copy(dateAdded = LocalDate.parse("2021-12-12")))
        }.isInstanceOf(WrongCarDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Car added date can't be after current date\"")
        assertThatThrownBy {
            carService.addCar(TestData.carToAdd1.copy(productionYear = 2024))
        }.isInstanceOf(WrongCarDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Car production year can't be after current year\"")
        every { carRepository.findBySerialNumber(123456) } returns TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }
        assertThatThrownBy {
            carService.addCar(TestData.carToAdd1.copy(serialNumber = 123456))
        }.isInstanceOf(CarSerialNumberAlreadyExistsException::class.java)
            .hasMessage("400 BAD_REQUEST \"Given car serial number already exists\"")
        val car = TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }
        every { carRepository.save(any()) } returns car.copy(id = 1)
        every { carRepository.findBySerialNumber(car.serialNumber) } returns null
        every {
            carModelService.getCarModelWithManufacturerAndModelName(
                car.carModel.manufacturer,
                car.carModel.modelName
            )
        } returns TestData.carModelToAdd1.toCarModel().copy(id = 1)
        assertThat(carService.addCar(TestData.carToAdd1)).isEqualTo(CarDto(car.copy(id = 1)))
        verify(exactly = 1) { carRepository.save(any()) }
    }

    @Test
    fun test3() {
        every { carRepository.findAll(any()) } returns PageImpl(listOf(
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 1)
            }.copy(id = 1),
            TestData.carToAdd2.toCar { _, _ ->
                TestData.carModelToAdd2.toCarModel().copy(id = 1)
            }.copy(id = 2)
        ))
        assertThat(carService.getAllCars(Pageable.unpaged())).isEqualTo(
            listOf(
                CarDto(TestData.carToAdd1.toCar { _, _ ->
                    TestData.carModelToAdd1.toCarModel().copy(id = 1)
                }.copy(id = 1)),
                CarDto(TestData.carToAdd2.toCar { _, _ ->
                    TestData.carModelToAdd2.toCarModel().copy(id = 1)
                }.copy(id = 2))
            )
        )
        verify(exactly = 1) { carRepository.findAll(any()) }
    }

    @Test
    fun test4() {
        every { carRepository.findAll(any()) } returns PageImpl(
            listOf(
                TestData.carToAdd1.toCar { _, _ ->
                    TestData.carModelToAdd1.toCarModel().copy(id = 1)
                }.copy(id = 1),
                TestData.carToAdd2.toCar { _, _ ->
                    TestData.carModelToAdd2.toCarModel().copy(id = 1)
                }.copy(id = 2),
                TestData.carToAdd3.toCar { _, _ ->
                    TestData.carModelToAdd3.toCarModel().copy(id = 1)
                }.copy(id = 3)
            )
        )
        assertThat(carService.getAllCars(Pageable.ofSize(20))).isEqualTo(
            PageImpl(
                listOf(
                    CarDto(TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)),
                    CarDto(TestData.carToAdd2.toCar { _, _ ->
                        TestData.carModelToAdd2.toCarModel().copy(id = 1)
                    }.copy(id = 2)),
                    CarDto(TestData.carToAdd3.toCar { _, _ ->
                        TestData.carModelToAdd3.toCarModel().copy(id = 1)
                    }.copy(id = 3))
                )
            )
        )
        verify(exactly = 1) { carRepository.findAll(any()) }
    }

}