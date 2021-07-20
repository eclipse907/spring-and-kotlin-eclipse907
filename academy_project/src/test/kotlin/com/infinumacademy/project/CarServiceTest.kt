package com.infinumacademy.project

import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarRepository
import com.infinumacademy.project.services.CarService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.format.DateTimeFormatter

class CarServiceTest {

    private val carRepository = mockk<CarRepository>()

    private lateinit var carService: CarService

    private val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    @BeforeEach
    fun setUp() {
        carService = CarService(carRepository)
    }

    @Test
    fun test1() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("23-12-2019 10:47:49", dateTimeFormat),
            "Alice",
            150.34,
            0
        )
        val car = Car(
            0,
            45,
            LocalDate.parse("01-02-2020", dateFormat),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
            mutableListOf(carCheckUp2, carCheckUp1)
        )
        every { carRepository.findById(0) } returns car
        assertThat(carService.getCarWithId(0)).isEqualTo(
            car.copy(
                carCheckUps = mutableListOf(
                    carCheckUp1,
                    carCheckUp2
                )
            )
        )
        every { carRepository.findById(1) } returns null
        assertThatThrownBy {
            carService.getCarWithId(1)
        }.isInstanceOf(CarNotFoundException::class.java).hasMessage("404 NOT_FOUND \"Car with id 1 not found\"")
        verify(exactly = 2) { carRepository.findById(any()) }
    }

    @Test
    fun test2() {
        val car1 = Car(
            null,
            45,
            LocalDate.parse("12-12-2021", dateFormat),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car1)
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessage("Car added date can't be after current date")
        val car2 = Car(
            null,
            45,
            LocalDate.parse("12-12-2020", dateFormat),
            "",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car2)
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessage("Manufacturer name can't be blank")
        val car3 = Car(
            null,
            45,
            LocalDate.parse("12-12-2020", dateFormat),
            "Toyota",
            "",
            Year.parse("2018"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car3)
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessage("Model name can't be blank")
        val car4 = Car(
            null,
            45,
            LocalDate.parse("12-12-2020", dateFormat),
            "Toyota",
            "Yaris",
            Year.parse("2022"),
            123456,
        )
        assertThatThrownBy {
            carService.addCar(car4)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Car production year can't be after current year")
        every { carRepository.insert(any()) } returns Unit
        every { carRepository.getAllCarIds() } returns mutableListOf()
        val car5 = Car(
            null,
            45,
            LocalDate.parse("01-02-2020", dateFormat),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        assertThat(carService.addCar(car5)).isEqualTo(car5.copy(id = 0))
        every { carRepository.getAllCarIds() } returns mutableListOf(25)
        val car6 = Car(
            null,
            45,
            LocalDate.parse("01-02-2020", dateFormat),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        assertThat(carService.addCar(car6)).isEqualTo(car5.copy(id = 26))
        verify(exactly = 2) { carRepository.insert(any()) }
        verify(exactly = 2) { carRepository.getAllCarIds() }
    }

    @Test
    fun test3() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("23-12-2019 10:47:49", dateTimeFormat),
            "Alice",
            150.34,
            0
        )
        val carCheckUp3 = CarCheckUp(
            2,
            LocalDateTime.parse("20-10-2018 12:05:10", dateTimeFormat),
            "Philip",
            234.09,
            0
        )
        val car1 = Car(
            0,
            45,
            LocalDate.parse("01-02-2020", dateFormat),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
            mutableListOf(carCheckUp3, carCheckUp1, carCheckUp2)
        )
        val car2 = Car(
            1,
            56,
            LocalDate.parse("03-09-2019", dateFormat),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654,
            mutableListOf(carCheckUp3.copy(carId = 1), carCheckUp2.copy(carId = 1), carCheckUp1.copy(carId = 1))
        )
        every { carRepository.getAllCars() } returns listOf(car1, car2)
        assertThat(carService.getAllCars()).isEqualTo(
            listOf(
                car1.copy(carCheckUps = mutableListOf(carCheckUp1, carCheckUp2, carCheckUp3)),
                car2.copy(
                    carCheckUps = mutableListOf(
                        carCheckUp1.copy(carId = 1),
                        carCheckUp2.copy(carId = 1),
                        carCheckUp3.copy(carId = 1)
                    )
                )
            )
        )
        verify(exactly = 1) { carRepository.getAllCars() }
    }

}