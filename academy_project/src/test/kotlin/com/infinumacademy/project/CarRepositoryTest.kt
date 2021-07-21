package com.infinumacademy.project

import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

class CarRepositoryTest {

    private val carRepository = CarRepository()

    @Test
    fun test1() {
        val car1 = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        val car2 = Car(
            1,
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654,
        )
        carRepository.save(car1)
        assertThat(carRepository.findById(0)).isEqualTo(car1)
        carRepository.save(car2)
        assertThat(carRepository.findById(1)).isEqualTo(car2)
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        carRepository.updateCarWithCarCheckUp(carCheckUp)
        assertThat(carRepository.findById(0)!!.carCheckUps).isEqualTo(listOf(carCheckUp))
        assertThatThrownBy {
            carRepository.updateCarWithCarCheckUp(carCheckUp.copy(carId = 45))
        }.isInstanceOf(CarNotFoundException::class.java).hasMessage("404 NOT_FOUND \"Car with id 45 not found\"")
    }

    @Test
    fun test2() {
        val car1 = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        val car2 = Car(
            1,
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654,
        )
        carRepository.save(car1)
        assertThat(carRepository.findById(0)).isEqualTo(car1)
        carRepository.save(car2)
        assertThat(carRepository.findById(1)).isEqualTo(car2)
        assertThat(carRepository.findAll()).isEqualTo(listOf(car1, car2))
        assertThat(carRepository.getAllCarIds()).isEqualTo(listOf(0L, 1L))
    }

}