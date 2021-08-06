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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
    private val carModelRepository: CarModelRepository
) {

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should throw 4xx codes when bad post request")
    fun test1() {
        carModelRepository.saveAll(
            listOf(
                TestData.carModelToAdd1.toCarModel(),
                TestData.carModelToAdd2.toCarModel(),
                TestData.carModelToAdd3.toCarModel()
            )
        )
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(dateAdded = LocalDate.parse("2021-12-12")))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Car added date can't be after current date\"")
            }
        }
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(manufacturerName = ""))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(modelName = ""))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(productionYear = 2025))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Car production year can't be after current year\"")
            }
        }
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(manufacturerName = "Void"))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"No car model found with given manufacturer and model name\"")
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should throw 404 not found when bad car id")
    fun test2() {
        mvc.get("/api/v1/cars/0").andExpect {
            status { isNotFound() }
            jsonPath("$.message") { value("404 NOT_FOUND \"Car with id 0 not found\"") }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should save and return cars when post and get request")
    fun test3() {
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/cars/1") }
        }
        mvc.get("/api/v1/cars/1").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
                    {
                        "id": 1,
                        "ownerId": 45,
                        "dateAdded": "2020-02-01",
                        "manufacturerName": "Toyota",
                        "modelName": "Yaris",
                        "productionYear": 2018,
                        "serialNumber": 123456,
                        "_links": {
                            "self": {
                                "href": "http://localhost/api/v1/cars/1"
                            },
                            "check-ups": {
                                "href": "http://localhost/api/v1/cars/1/car-check-ups"
                            }
                        }
                    }
                """.trimIndent()
                )
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should throw 400 bad request when post has existent serial number")
    fun test4() {
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd2.copy(serialNumber = TestData.carToAdd1.serialNumber))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("400 BAD_REQUEST \"Given car serial number already exists\"")
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should return all cars when get all cars request")
    fun test5() {
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/cars/2") }
        }
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/cars/3") }
        }
        mvc.get("/api/v1/cars").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
                                {
                                    "_embedded": {
                                        "item": [
                                            {
                                    "id": 1,
                                    "ownerId": 45,
                                    "dateAdded": "2020-02-01",
                                    "manufacturerName": "Toyota",
                                    "modelName": "Yaris",
                                    "productionYear": 2018,
                                    "serialNumber": 123456,
                                    "_links": {
                                        "self": {
                                            "href": "http://localhost/api/v1/cars/1"
                                        },
                                        "check-ups": {
                                            "href": "http://localhost/api/v1/cars/1/car-check-ups"
                                        }
                                    }
                                },
                                {
                                    "id": 2,
                                    "ownerId": 56,
                                    "dateAdded": "2019-09-03",
                                    "manufacturerName": "Opel",
                                    "modelName": "Astra",
                                    "productionYear": 2016,
                                    "serialNumber": 654321,
                                    "_links": {
                                        "self": {
                                            "href": "http://localhost/api/v1/cars/2"
                                        },
                                        "check-ups": {
                                            "href": "http://localhost/api/v1/cars/2/car-check-ups"
                                        }
                                    }
                                },
                                {
                                    "id": 3,
                                    "ownerId": 78,
                                    "dateAdded": "2021-02-01",
                                    "manufacturerName": "Toyota",
                                    "modelName": "Corolla",
                                    "productionYear": 2020,
                                    "serialNumber": 654123,
                                    "_links": {
                                        "self": {
                                            "href": "http://localhost/api/v1/cars/3"
                                        },
                                        "check-ups": {
                                            "href": "http://localhost/api/v1/cars/3/car-check-ups"
                                        }
                                    }
                                }
                            ]
                        },
                        "_links": {
                            "self": {
                                "href": "http://localhost/api/v1/cars?page=0&size=20"
                            }
                        },
                        "page": {
                            "size": 20,
                            "totalElements": 3,
                            "totalPages": 1,
                            "number": 0
                        }
                    }
                """.trimIndent()
                )
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should delete car when delete request has good car id")
    fun test6() {
        mvc.delete("/api/v1/cars/1").andExpect { status { isAccepted() } }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should throw 404 not found when bad car id in delete request")
    fun test7() {
        mvc.delete("/api/v1/cars/34").andExpect { status { isNotFound() } }
    }

    @Test
    @DisplayName("should throw 401 unauthorized when not logged in")
    fun test8() {
        mvc.post("/api/v1/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1.copy(dateAdded = LocalDate.parse("2021-12-12")))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized() }
        }
        mvc.get("/api/v1/cars/1").andExpect { status { isUnauthorized() } }
        mvc.get("/api/v1/cars").andExpect { status { isUnauthorized() } }
        mvc.delete("/api/v1/cars/1").andExpect { status { isUnauthorized() } }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    @DisplayName("should throw 403 forbidden when logged in as user")
    fun test9() {
        mvc.get("/api/v1/cars").andExpect { status { isForbidden() } }
        mvc.delete("/api/v1/cars/1").andExpect { status { isForbidden() } }
    }

}