package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.controllers.CarController
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarDataException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.services.CarService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.lang.NullPointerException
import java.time.LocalDate
import java.time.Year

@WebMvcTest(CarController::class)
class CarControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @MockkBean
    private lateinit var carService: CarService

    @Test
    fun test1() {
        val car = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        every { carService.getCarWithId(0) } returns car
        mvc.get("/cars/0").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(car)) }
        }
        verify(exactly = 1) { carService.getCarWithId(0) }
    }

    @Test
    fun test2() {
        val car = Car(
            null,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        every { carService.addCar(car) } returns car.copy(id = 0)
        every { carService.getCarWithId(0) } returns car.copy(id = 0)
        mvc.post("/cars") {
            content = mapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/0") }
        }
        mvc.get("/cars/0").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(car.copy(id = 0))) }
        }
        verify(exactly = 1) { carService.addCar(car) }
        verify(exactly = 1) { carService.getCarWithId(0) }
    }

    @Test
    fun test3() {
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
        every { carService.getAllCars() } returns listOf(car1, car2)
        mvc.get("/cars").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(listOf(car1, car2))) }
        }
        verify(exactly = 1) { carService.getAllCars() }
    }

    @Test
    fun test4() {
        every { carService.getCarWithId(0) } throws CarNotFoundException(0)
        mvc.get("/cars/0").andExpect {
            status { isNotFound() }
        }
        val car = Car(
            null,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "",
            Year.parse("2018"),
            123456
        )
        every { carService.addCar(car) } throws WrongCarDataException("Model name can't be blank")
        mvc.post("/cars") {
            content = mapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
        verify(exactly = 1) { carService.getCarWithId(0) }
        verify(exactly = 1) { carService.addCar(car) }
    }

    @Test
    fun test5() {
        val car = Car(
            null,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        every { carService.addCar(car) } throws NullPointerException("Car id is null")
        mvc.post("/cars") {
            content = mapper.writeValueAsString(car)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isInternalServerError() }
            jsonPath("$.message") { value("Car id is null") }
        }
        verify(exactly = 1) { carService.addCar(car) }
    }
}