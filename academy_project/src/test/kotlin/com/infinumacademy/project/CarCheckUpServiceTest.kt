package com.infinumacademy.project

import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.WrongCarCheckUpCarIdException
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import com.infinumacademy.project.services.CarCheckUpService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class CarCheckUpServiceTest {

    private val carCheckUpRepository = mockk<CarCheckUpRepository>()
    private val carRepository = mockk<CarRepository>()

    private lateinit var carCheckUpService: CarCheckUpService

    @BeforeEach
    fun setUp() {
        carCheckUpService = CarCheckUpService(carCheckUpRepository, carRepository)
    }

    @Test
    fun test1() {
        val carCheckUp = TestData.carCheckUpToAdd1.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 1)
            }.copy(id = 1)
        }.copy(id = 1)
        every { carCheckUpRepository.findById(1) } returns carCheckUp
        assertThat(carCheckUpService.getCarCheckUpWithId(1)).isEqualTo(CarCheckUpDto(carCheckUp))
        every { carCheckUpRepository.findById(2) } returns null
        assertThatThrownBy {
            carCheckUpService.getCarCheckUpWithId(2)
        }.isInstanceOf(CarCheckUpNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Car check-up with id 2 not found\"")
        verify(exactly = 2) { carCheckUpRepository.findById(any()) }
    }

    @Test
    fun test2() {
        every { carRepository.findById(1) } returns TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }.copy(id = 1)
        every { carRepository.findById(47) } returns null
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(TestData.carCheckUpToAdd1.copy(carId = 47))
        }.isInstanceOf(WrongCarCheckUpCarIdException::class.java)
            .hasMessage("400 BAD_REQUEST \"No car with given id found\"")
        val carCheckUp = TestData.carCheckUpToAdd1.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 1)
            }.copy(id = 1)
        }
        every { carCheckUpRepository.save(any()) } returns carCheckUp.copy(id = 1)
        assertThat(carCheckUpService.addCarCheckUp(TestData.carCheckUpToAdd1)).isEqualTo(
            CarCheckUpDto(carCheckUp.copy(id = 1))
        )
        verify(exactly = 1) { carCheckUpRepository.save(any()) }
        verify(exactly = 2) { carRepository.findById(any()) }
    }

    @Test
    fun test3() {
        every { carCheckUpRepository.findAllByOrderByTimeOfCheckUpDesc(any()) } returns PageImpl(
            listOf(
                TestData.carCheckUpToAdd1.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }.copy(id = 1),
                TestData.carCheckUpToAdd2.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }.copy(id = 2),
                TestData.carCheckUpToAdd3.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }.copy(id = 3)
            )
        )
        assertThat(carCheckUpService.getAllCarCheckUps(Pageable.unpaged())).isEqualTo(PageImpl(listOf(
            CarCheckUpDto(TestData.carCheckUpToAdd1.toCarCheckUp {
                TestData.carToAdd1.toCar { _, _ ->
                    TestData.carModelToAdd1.toCarModel().copy(id = 1)
                }.copy(id = 1)
            }
                .copy(id = 1)),
            CarCheckUpDto(TestData.carCheckUpToAdd2.toCarCheckUp {
                TestData.carToAdd1.toCar { _, _ ->
                    TestData.carModelToAdd1.toCarModel().copy(id = 1)
                }.copy(id = 1)
            }
                .copy(id = 2)),
            CarCheckUpDto(TestData.carCheckUpToAdd3.toCarCheckUp {
                TestData.carToAdd1.toCar { _, _ ->
                    TestData.carModelToAdd1.toCarModel().copy(id = 1)
                }.copy(id = 1)
            }
                .copy(id = 3))
        ))
        )
        verify(exactly = 1) { carCheckUpRepository.findAllByOrderByTimeOfCheckUpDesc(any()) }
    }

    @Test
    fun test4() {
        every { carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(1, any()) } returns PageImpl(
            listOf(
                TestData.carCheckUpToAdd1.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }.copy(id = 1),
                TestData.carCheckUpToAdd2.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }.copy(id = 2),
                TestData.carCheckUpToAdd3.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }.copy(id = 3)
            )
        )
        every { carRepository.findById(1) } returns TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }.copy(id = 1)
        assertThat(carCheckUpService.getAllCarCheckUpsWithCarId(1, Pageable.ofSize(20))).isEqualTo(
            PageImpl(listOf(
                CarCheckUpDto(TestData.carCheckUpToAdd1.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }
                    .copy(id = 1)),
                CarCheckUpDto(TestData.carCheckUpToAdd2.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }
                    .copy(id = 2)),
                CarCheckUpDto(TestData.carCheckUpToAdd3.toCarCheckUp {
                    TestData.carToAdd1.toCar { _, _ ->
                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                    }.copy(id = 1)
                }
                    .copy(id = 3))
            ))
        )
    }

}
