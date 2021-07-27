package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.CarDto
import com.infinumacademy.project.dtos.CarWithCarCheckUpsDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

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
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(dateAdded = LocalDate.parse("2021-12-12")))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Car added date can't be after current date\"")
            }
        }
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(manufacturerName = ""))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(modelName = ""))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(productionYear = 2025))
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
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/1") }
        }
        mvc.get("/cars/1").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    mapper.writeValueAsString(
                        CarWithCarCheckUpsDto(TestData.carToAdd1.toCar().copy(id = 1), listOf())
                    )
                )
            }
        }
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd2.copy(serialNumber = TestData.carToAdd1.serialNumber))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun test3() {
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/3") }
        }
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/4") }
        }
        mvc.get("/cars").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    mapper.writeValueAsString(
                        listOf(
                            CarDto(TestData.carToAdd1.toCar().copy(id = 1)),
                            CarDto(TestData.carToAdd2.toCar().copy(id = 3)),
                            CarDto(TestData.carToAdd3.toCar().copy(id = 4))
                        )
                    )
                )
            }
        }
    }

    @Test
    fun test5() {
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3.copy(carId = 3))
            contentType = MediaType.APPLICATION_JSON
        }
        mvc.get("/cars/1").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    mapper.writeValueAsString(
                        CarWithCarCheckUpsDto(
                            TestData.carToAdd1.toCar().copy(id = 1),
                            listOf(
                                TestData.carCheckUpToAdd1.toCarCheckUp { TestData.carToAdd1.toCar().copy(id = 1) }
                                    .copy(id = 2),
                                TestData.carCheckUpToAdd2.toCarCheckUp { TestData.carToAdd1.toCar().copy(id = 1) }
                                    .copy(id = 1)
                            )
                        )
                    )
                )
            }
        }
        mvc.get("/cars/3").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    mapper.writeValueAsString(
                        CarWithCarCheckUpsDto(
                            TestData.carToAdd2.toCar().copy(id = 3),
                            listOf(
                                TestData.carCheckUpToAdd3.copy(carId = 3)
                                    .toCarCheckUp { TestData.carToAdd2.toCar().copy(id = 3) }
                                    .copy(id = 3))
                        )
                    )
                )
            }
        }
    }

    @Test
    fun test6() {
        mvc.get("/cars/paged").andExpect {
            status { is2xxSuccessful() }
            content {
                jsonPath("$.content") {
                    value(
                        mapper.writeValueAsString(
                            listOf(
                                CarDto(TestData.carToAdd1.toCar().copy(id = 1)),
                                CarDto(TestData.carToAdd2.toCar().copy(id = 3)),
                                CarDto(TestData.carToAdd3.toCar().copy(id = 4))
                            )
                        )
                    )
                }
                jsonPath("$.totalElements") { value(3) }
            }
        }
    }

}