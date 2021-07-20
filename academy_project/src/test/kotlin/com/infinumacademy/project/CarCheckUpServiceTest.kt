package com.infinumacademy.project

import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.services.CarCheckUpService
import com.infinumacademy.project.services.CarService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CarCheckUpServiceTest {

    private val carCheckUpRepository = mockk<CarCheckUpRepository>()
    private val carService = mockk<CarService>()

    private lateinit var carCheckUpService: CarCheckUpService

    private val dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    @BeforeEach
    fun setUp() {
        carCheckUpService = CarCheckUpService(carCheckUpRepository, carService)
    }

    @Test
    fun test1() {
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
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
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        every { carCheckUpRepository.findById(carCheckUp1.id) } returns carCheckUp1
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp1)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Car check-up with id ${carCheckUp1.id} already exists")
        every { carCheckUpRepository.findById(any()) } returns null
        val carCheckUp2 = CarCheckUp(
            0,
            LocalDateTime.parse("15-10-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp2)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Date and time of check-up can't be after current date and time")
        val carCheckUp3 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "",
            23.56,
            0
        )
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp3)
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessage("Worker name can't be blank")
        val carCheckUp4 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            -23.56,
            0
        )
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp4)
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessage("Price can't be less than zero")
        val carCheckUp5 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        every { carService.addCarCheckUpToCar(carCheckUp5) } throws CarNotFoundException(carCheckUp5.carId)
        assertThatThrownBy {
            carCheckUpService.addCarCheckUp(carCheckUp5)
        }.isInstanceOf(CarNotFoundException::class.java).hasMessage("404 NOT_FOUND \"Car with id 0 not found\"")
        val carCheckUp6 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        every { carService.addCarCheckUpToCar(carCheckUp6) } returns Unit
        every { carCheckUpRepository.insert(carCheckUp6) } returns Unit
        assertThat(carCheckUpService.addCarCheckUp(carCheckUp6)).isEqualTo(carCheckUp6)
        verify(exactly = 6) { carCheckUpRepository.findById(any()) }
        verify(exactly = 2) { carService.addCarCheckUpToCar(any()) }
        verify(exactly = 1) { carCheckUpRepository.insert(carCheckUp6) }
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
        every { carCheckUpRepository.getAllCarCheckUps() } returns listOf(carCheckUp2, carCheckUp1, carCheckUp3)
        assertThat(carCheckUpService.getAllCarCheckUps()).isEqualTo(listOf(carCheckUp1, carCheckUp2, carCheckUp3))
        verify(exactly = 1) { carCheckUpRepository.getAllCarCheckUps() }
    }

}