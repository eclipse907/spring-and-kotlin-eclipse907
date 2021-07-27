package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.CarCheckUpDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

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
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/1") }
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/1") }
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(
                TestData.carCheckUpToAdd1.copy(
                    timeOfCheckUp = LocalDateTime.parse("2021-10-15T20:35:10")
                )
            )
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Date and time of check-up can't be after current date and time\"")
            }
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1.copy(workerName = ""))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1.copy(price = -45.27))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1.copy(carId = 45))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"No car with given id found\"")
            }
        }
    }

    @Test
    fun test2() {
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/2") }
        }
        mvc.get("/car-checkups/2").andExpect {
            status { is2xxSuccessful() }
            content {
                json(mapper.writeValueAsString(CarCheckUpDto(TestData.carCheckUpToAdd2.toCarCheckUp {
                    TestData.carToAdd1.toCar().copy(id = 1)
                }.copy(id = 2))))
            }
        }
    }

    @Test
    fun test3() {
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/3") }
        }
        mvc.get("/car-checkups").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    mapper.writeValueAsString(
                        listOf(
                            CarCheckUpDto(TestData.carCheckUpToAdd2.toCarCheckUp {
                                TestData.carToAdd1.toCar().copy(id = 1)
                            }.copy(id = 2)),
                            CarCheckUpDto(TestData.carCheckUpToAdd3.toCarCheckUp {
                                TestData.carToAdd1.toCar().copy(id = 1)
                            }.copy(id = 3)),
                            CarCheckUpDto(TestData.carCheckUpToAdd1.toCarCheckUp {
                                TestData.carToAdd1.toCar().copy(id = 1)
                            }.copy(id = 1))
                        )
                    )
                )
            }
        }
    }

    @Test
    fun test4() {
        mvc.get("/car-checkups/car-id/1").andExpect {
            status { is2xxSuccessful() }
            content {
                jsonPath("$.content") {
                    value(
                        mapper.writeValueAsString(
                            listOf(
                                CarCheckUpDto(TestData.carCheckUpToAdd1.toCarCheckUp {
                                    TestData.carToAdd1.toCar().copy(id = 1)
                                }.copy(id = 1)),
                                CarCheckUpDto(TestData.carCheckUpToAdd2.toCarCheckUp {
                                    TestData.carToAdd1.toCar().copy(id = 1)
                                }.copy(id = 2)),
                                CarCheckUpDto(TestData.carCheckUpToAdd3.toCarCheckUp {
                                    TestData.carToAdd1.toCar().copy(id = 1)
                                }.copy(id = 3))
                            )
                        )
                    )
                }
                jsonPath("$.totalElements") { value(3) }
            }
        }
    }

}