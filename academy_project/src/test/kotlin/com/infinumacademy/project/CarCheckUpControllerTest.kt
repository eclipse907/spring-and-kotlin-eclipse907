package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.controllers.CarCheckUpController
import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.services.CarCheckUpService
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest(CarCheckUpController::class)
class CarCheckUpControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @MockkBean
    private lateinit var carCheckUpService: CarCheckUpService

    private val dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    @Test
    fun test1() {
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        every { carCheckUpService.getCarCheckUpWithId(0) } returns carCheckUp
        mvc.get("/car-checkups/0").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(carCheckUp)) }
        }
        verify(exactly = 1) { carCheckUpService.getCarCheckUpWithId(0) }
    }

    @Test
    fun test2() {
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        every { carCheckUpService.addCarCheckUp(carCheckUp) } returns carCheckUp
        every { carCheckUpService.getCarCheckUpWithId(0) } returns carCheckUp
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUp)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/0") }
        }
        mvc.get("/car-checkups/0").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(carCheckUp)) }
        }
        verify(exactly = 1) { carCheckUpService.getCarCheckUpWithId(0) }
        verify(exactly = 1) { carCheckUpService.addCarCheckUp(carCheckUp) }
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
            1
        )
        every { carCheckUpService.getAllCarCheckUps() } returns listOf(carCheckUp2, carCheckUp1)
        mvc.get("/car-checkups").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(listOf(carCheckUp1, carCheckUp2))) }
        }
        verify(exactly = 1) { carCheckUpService.getAllCarCheckUps() }
    }

    @Test
    fun test4() {
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2025 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        every { carCheckUpService.addCarCheckUp(carCheckUp) } throws IllegalArgumentException(
            "Date and time of check-up can't be after current date and time"
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUp)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") { value("Date and time of check-up can't be after current date and time") }
        }
        every { carCheckUpService.getCarCheckUpWithId(0) } throws CarCheckUpNotFoundException(0)
        mvc.get("/car-checkups/0").andExpect {
            status { isNotFound() }
        }
        verify(exactly = 1) { carCheckUpService.addCarCheckUp(carCheckUp) }
        verify(exactly = 1) { carCheckUpService.getCarCheckUpWithId(0) }
    }

    @Test
    fun test5() {
        val carCheckUp = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            766
        )
        every { carCheckUpService.addCarCheckUp(carCheckUp) } throws CarNotFoundException(766)
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUp)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
        verify(exactly = 1) { carCheckUpService.addCarCheckUp(carCheckUp) }
    }

}