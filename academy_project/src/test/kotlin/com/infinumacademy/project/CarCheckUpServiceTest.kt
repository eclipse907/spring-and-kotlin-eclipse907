package com.infinumacademy.project

import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.WrongCarCheckUpCarIdException
import com.infinumacademy.project.exceptions.WrongCarCheckUpDataException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

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
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        every { carCheckUpRepository.findById(0) } returns carCheckUp
        assertThat(carCheckUpService.getCarCheckUpWithId(0)).isEqualTo(carCheckUp)
        every { carCheckUpRepository.findById(1) } returns null
        assertThatThrownBy {
            carCheckUpService.getCarCheckUpWithId(1)
        }.isInstanceOf(CarCheckUpNotFoundException::class.java)
            .hasMessage("404 NOT_FOUND \"Car check-up with id 1 not found\"")
        verify(exactly = 1) { carCheckUpRepository.findById(0) }
        verify(exactly = 1) { carCheckUpRepository.findById(1) }
    }

    @Test
    fun test2() {
        val carCheckUp1 = CarCheckUp(
            25,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        every { carCheckUpRepository.findById(carCheckUp1.id) } returns carCheckUp1
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp1)
        }.isInstanceOf(WrongCarCheckUpDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Car check-up with id ${carCheckUp1.id} already exists\"")
        every { carCheckUpRepository.findById(any()) } returns null
        val carCheckUp2 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-10-15T20:35:10"),
            "Bob",
            23.56,
            0
        )
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp2)
        }.isInstanceOf(WrongCarCheckUpDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Date and time of check-up can't be after current date and time\"")
        val carCheckUp3 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "",
            23.56,
            0
        )
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp3)
        }.isInstanceOf(WrongCarCheckUpDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Worker name can't be blank\"")
        val carCheckUp4 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            -23.56,
            0
        )
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp4)
        }.isInstanceOf(WrongCarCheckUpDataException::class.java)
            .hasMessage("400 BAD_REQUEST \"Price can't be less than zero\"")
        val carCheckUp5 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        every { carRepository.findById(carCheckUp5.carId) } returns null
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp5)
        }.isInstanceOf(WrongCarCheckUpCarIdException::class.java)
            .hasMessage("400 BAD_REQUEST \"Car check-up has non existent car id\"")
        val carCheckUp6 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        val car = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
            mutableListOf(carCheckUp2, carCheckUp1)
        )
        every { carRepository.findById(carCheckUp6.carId) } returns car
        every { carCheckUpRepository.save(carCheckUp6) } returns Unit
        assertThat(carCheckUpService.addCarCheckUp(carCheckUp6)).isEqualTo(carCheckUp6)
        verify(exactly = 6) { carCheckUpRepository.findById(any()) }
        verify(exactly = 2) { carRepository.findById(any()) }
        verify(exactly = 1) { carCheckUpRepository.save(carCheckUp6) }
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
        every { carCheckUpRepository.findAll() } returns listOf(carCheckUp2, carCheckUp1, carCheckUp3)
        assertThat(carCheckUpService.getAllCarCheckUps()).isEqualTo(listOf(carCheckUp1, carCheckUp2, carCheckUp3))
        verify(exactly = 1) { carCheckUpRepository.findAll() }
    }

}
