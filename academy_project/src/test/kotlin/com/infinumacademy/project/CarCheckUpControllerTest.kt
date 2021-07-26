package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.CarCheckUpRequestDto
import com.infinumacademy.project.dtos.CarRequestDto
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
class CarCheckUpControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @Test
    fun test1() {
        mvc.get("/car-checkups/0").andExpect {
            status { isNotFound() }
            jsonPath("$.message") {
                value("404 NOT_FOUND \"Car check-up with id 0 not found\"")
            }
        }
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
        val carCheckUpToAdd1 = CarCheckUpRequestDto(
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/1") }
        }
        val carCheckUpToAdd2 = CarCheckUpRequestDto(
            LocalDateTime.parse("2021-10-15T20:35:10"),
            "Bob",
            23.56,
            1
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Date and time of check-up can't be after current date and time\"")
            }
        }
        val carCheckUpToAdd3 = CarCheckUpRequestDto(
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "",
            23.56,
            1
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Worker name can't be blank\"")
            }
        }
        val carCheckUpToAdd4 = CarCheckUpRequestDto(
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            -23.56,
            1
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd4)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Price can't be less than zero\"")
            }
        }
        val carCheckUpToAdd5 = CarCheckUpRequestDto(
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            45
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd5)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun test2() {
        val carCheckUpToAdd = CarCheckUpRequestDto(
            LocalDateTime.parse("2018-12-23T10:30:10"),
            "Bob",
            23.56,
            1
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/3") }
        }
        mvc.get("/car-checkups/3").andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(carCheckUpToAdd.toCarCheckUp().copy(id = 3))) }
        }
    }

    @Test
    fun test3() {
        val carCheckUp1 = CarCheckUp(
            1,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUp2 = CarCheckUp(
            3,
            LocalDateTime.parse("2018-12-23T10:30:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUpToAdd3 = CarCheckUpRequestDto(
            LocalDateTime.parse("2017-08-06T15:05:30"),
            "Bob",
            23.56,
            1
        )
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/4") }
        }
        mvc.get("/car-checkups").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    mapper.writeValueAsString(
                        listOf(
                            carCheckUp2,
                            carCheckUpToAdd3.toCarCheckUp().copy(id = 4),
                            carCheckUp1
                        )
                    )
                )
            }
        }
    }

}