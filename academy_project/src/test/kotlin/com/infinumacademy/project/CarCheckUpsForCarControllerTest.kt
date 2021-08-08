package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.repositories.CarModelRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarCheckUpsForCarControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
    private val carModelRepository: CarModelRepository
) {

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should return car check-ups for car with id 1 when called")
    fun test1() {
        carModelRepository.saveAll(
            listOf(
                TestData.carModelToAdd1.toCarModel(),
                TestData.carModelToAdd2.toCarModel(),
                TestData.carModelToAdd3.toCarModel()
            )
        )
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/cars/1") }
        }
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/1") }
        }
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/2") }
        }
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/3") }
        }
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/cars/2") }
        }
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3.copy(carId = 2))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/4") }
        }
        mvc.get("/api/v1/cars/1/car-check-ups").andExpect {
            status { is2xxSuccessful() }
            content {
                json("""
                    {
                        "_embedded": {
                            "item": [
                                    {
                                        "id": 1,
                                        "timeOfCheckUp": "2021-06-06T20:35:10",
                                        "workerName": "Bob",
                                        "price": 23.56,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/1"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    },
                                    {
                                        "id": 2,
                                        "timeOfCheckUp": "2018-12-23T10:30:10",
                                        "workerName": "Tom",
                                        "price": 57.34,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/2"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    },
                                    {
                                        "id": 3,
                                        "timeOfCheckUp": "2016-08-06T15:05:30",
                                        "workerName": "Adam",
                                        "price": 45.97,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/3"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    }
                            ]
                        },
                        "_links": {
                            "self": {
                                "href": "http://localhost/api/v1/cars/1/car-check-ups?page=0&size=20"
                            }
                        },
                        "page": {
                            "size": 20,
                            "totalElements": 3,
                            "totalPages": 1,
                            "number": 0
                        }
                    }
                """.trimIndent())
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    @DisplayName("should throw 404 not found when non existent car id called")
    fun test2() {
        mvc.get("/api/v1/cars/56/car-check-ups").andExpect {
            status { isNotFound() }
        }
    }

    @Test
    @DisplayName("should throw 401 unauthorized when not logged in")
    fun test3() {
        mvc.get("/api/v1/cars/56/car-check-ups").andExpect {
            status { isUnauthorized() }
        }
    }

}