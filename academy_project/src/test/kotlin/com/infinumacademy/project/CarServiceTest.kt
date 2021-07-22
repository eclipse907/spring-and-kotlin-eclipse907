package com.infinumacademy.project

import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarDataException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import com.infinumacademy.project.services.CarService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

class CarServiceTest {

    private val carRepository = mockk<CarRepository>()
    private val carCheckUpRepository = mockk<CarCheckUpRepository>()

    private lateinit var carService: CarService

    @BeforeEach
    fun setUp() {
        carService = CarService(carRepository, carCheckUpRepository)
    }

    @Test
    fun test1() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("2019-12-23T10:47:49"),
            "Alice",
            150.34,
            0
        )
        val car = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        every { carRepository.findById(0) } returns car
        every { carCheckUpRepository.findByCarId(0) } returns listOf(carCheckUp2, carCheckUp1)
        assertThat(carService.getCarWithId(0)).isEqualTo(
            car.copy(
                carCheckUps = mutableListOf(
                    carCheckUp1,
                    carCheckUp2
                )
            )
        )
        every { carRepository.findById(1) } returns null
        every { carCheckUpRepository.findByCarId(1) } returns listOf()
        assertThatThrownBy {
            carService.getCarWithId(1)
        }.isInstanceOf(CarNotFoundException::class.java).hasMessage("404 NOT_FOUND \"Car with id 1 not found\"")
        verify(exactly = 2) { carRepository.findById(any()) }
    }

    @Test
    fun test2() {
        val car1 = Car(
            0,
            45,
            LocalDate.parse("2021-12-12"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car1)
        }.isInstanceOf(WrongCarDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Car added date can't be after current date\"")
        val car2 = Car(
            0,
            45,
            LocalDate.parse("2020-12-12"),
            "",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car2)
        }.isInstanceOf(WrongCarDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Manufacturer name can't be blank\"")
        val car3 = Car(
            0,
            45,
            LocalDate.parse("2020-12-12"),
            "Toyota",
            "",
            Year.parse("2018"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car3)
        }.isInstanceOf(WrongCarDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Model name can't be blank\"")
        val car4 = Car(
            0,
            45,
            LocalDate.parse("2020-12-12"),
            "Toyota",
            "Yaris",
            Year.parse("2022"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car4)
        }.isInstanceOf(WrongCarDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Car production year can't be after current year\"")
        val car5 = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        every { carRepository.save(car5) } returns car5.id
        every { carCheckUpRepository.findByCarId(car5.id) } returns listOf()
        assertThat(carService.addCar(car5)).isEqualTo(car5.copy())
        verify(exactly = 1) { carRepository.save(any()) }
    }

    @Test
    fun test3() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("2019-12-23T10:47:49"),
            "Alice",
            150.34,
            0
        )
        val carCheckUp3 = CarCheckUp(
            2,
            LocalDateTime.parse("2018-10-20T12:05:10"),
            "Philip",
            234.09,
            0
        )
        val car1 = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        val car2 = Car(
            1,
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654
        )
        every { carRepository.findAll() } returns listOf(car1, car2)
        every { carCheckUpRepository.findByCarId(car1.id) } returns listOf(carCheckUp3, carCheckUp1, carCheckUp2)
        every { carCheckUpRepository.findByCarId(car2.id) } returns listOf(carCheckUp3.copy(), carCheckUp2.copy(), carCheckUp1.copy())
        assertThat(carService.getAllCars()).isEqualTo(
            listOf(
                car1.copy(carCheckUps = mutableListOf(carCheckUp1, carCheckUp2, carCheckUp3)),
                car2.copy(
                    carCheckUps = mutableListOf(
                        carCheckUp1.copy(),
                        carCheckUp2.copy(),
                        carCheckUp3.copy()
                    )
                )
            )
        )
        verify(exactly = 1) { carRepository.findAll() }
    }

}