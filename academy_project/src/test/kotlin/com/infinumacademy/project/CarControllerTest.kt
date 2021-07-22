package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.CarRequestDto
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @Test
    fun test1() {
        mvc.get("/cars/0").andExpect {
            status { isNotFound() }
            jsonPath("$.message") { value("404 NOT_FOUND \"Car with id 0 not found\"") }
        }
        val carToAdd1 = CarRequestDto(
            45,
            LocalDate.parse("2021-12-12"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Car added date can't be after current date\"")
            }
        }
        val carToAdd2 = CarRequestDto(
            45,
            LocalDate.parse("2020-12-12"),
            "",
            "Yaris",
            Year.parse("2018"),
            123456,
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Manufacturer name can't be blank\"")
            }
        }
        val carToAdd3 = CarRequestDto(
            45,
            LocalDate.parse("2020-12-12"),
            "Toyota",
            "",
            Year.parse("2018"),
            123456,
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Model name can't be blank\"")
            }
        }
        val carToAdd4 = CarRequestDto(
            45,
            LocalDate.parse("2020-12-12"),
            "Toyota",
            "Yaris",
            Year.parse("2022"),
            123456,
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd4)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Car production year can't be after current year\"")
            }
        }
    }

    @Test
    fun test2() {
        val carToAdd = CarRequestDto(
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/1") }
        }
        mvc.get("/cars/1").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(carToAdd.toCar().copy(id = 1))) }
        }
    }

    @Test
    fun test3() {
        val car1 = Car(
            1,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        val carToAdd = CarRequestDto(
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/2") }
        }
        mvc.get("/cars").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(listOf(car1, carToAdd.toCar().copy(id = 2)))) }
        }
    }

    @Test
    fun test4() {
        val car1 = Car(
            1,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        val car2 = Car(
            2,
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654
        )
        val carToAdd = CarRequestDto(
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Corolla",
            Year.parse("2018"),
            123456
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/3") }
        }
        mvc.get("/cars").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(listOf(car1, car2, carToAdd.toCar().copy(id = 3)))) }
        }
    }

    @Test
    fun test5() {
        val carToAdd = CarRequestDto(
            4654,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Corolla",
            Year.parse("2018"),
            123456
        )
        mvc.post("/cars") {
            content = mapper.writeValueAsString(carToAdd)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/4") }
        }
        val carCheckUp1 = CarCheckUp(
            25,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            4
        )
        val carCheckUp2 = CarCheckUp(
            34,
            LocalDateTime.parse("2018-12-23T10:30:10"),
            "Bob",
            23.56,
            4
        )
        val carCheckUp3 = CarCheckUp(
            63,
            LocalDateTime.parse("2017-08-06T15:05:30"),
            "Bob",
            23.56,
            4
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUp2)
            contentType = MediaType.APPLICATION_JSON
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUp1)
            contentType = MediaType.APPLICATION_JSON
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUp3)
            contentType = MediaType.APPLICATION_JSON
        }
        val carToCheck = Car(
            4,
            4654,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Corolla",
            Year.parse("2018"),
            123456,
            mutableListOf(carCheckUp1, carCheckUp2, carCheckUp3)
        )
        mvc.get("/cars/4").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(carToCheck)) }
        }
    }

}