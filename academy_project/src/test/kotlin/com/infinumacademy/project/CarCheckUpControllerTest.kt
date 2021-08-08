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
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarCheckUpControllerTest @Autowired constructor(
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
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1.copy(workerName = ""))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd1.copy(price = -45.27))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") {
                value("Bad post request arguments")
            }
        }
        mvc.post("/api/v1/car-checkups") {
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
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should save cars when good post request")
    fun test2() {
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
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should get cars when good get request")
    fun test3() {
        mvc.get("/api/v1/car-checkups/1").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
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
                    }
                """.trimIndent()
                )
            }
        }
        mvc.get("/api/v1/car-checkups/2").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
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
                    }
                """.trimIndent()
                )
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should throw 404 not found when bad car check-up id")
    fun test4() {
        mvc.get("/api/v1/car-checkups/56").andExpect {
            status { isNotFound() }
            jsonPath("$.message") {
                value("404 NOT_FOUND \"Car check-up with id 56 not found\"")
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should return all car check-ups")
    fun test5() {
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/3") }
        }
        mvc.get("/api/v1/car-checkups").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
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
                                    "href": "http://localhost/api/v1/car-checkups?page=0&size=20"
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
    @DisplayName("should throw 401 unauthorized when not logged in")
    fun test6() {
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isUnauthorized() } }
        mvc.get("/api/v1/car-checkups/1").andExpect { status { isUnauthorized() } }
        mvc.get("/api/v1/car-checkups").andExpect { status { isUnauthorized() } }
        mvc.get("/api/v1/car-checkups/performed/last-ten").andExpect { status { isUnauthorized() } }
        mvc.get("/api/v1/car-checkups/upcoming").andExpect { status { isUnauthorized() } }
        mvc.delete("/api/v1/car-checkups/1").andExpect { status { isUnauthorized() } }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should return last 3 car check-ups when called")
    fun test7() {
        mvc.get("/api/v1/car-checkups/performed/last-ten").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
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
                            }
                        }
                    """.trimIndent()
                )
            }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    @DisplayName("should throw 400 bad request when wrong duration")
    fun test8() {
        mvc.get("/api/v1/car-checkups/upcoming?duration=edrgerdgr").andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_USER"])
    @DisplayName("should throw 403 forbidden when logged in as user")
    fun test9() {
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isForbidden() } }
        mvc.get("/api/v1/car-checkups/1").andExpect { status { isForbidden() } }
        mvc.get("/api/v1/car-checkups").andExpect { status { isForbidden() } }
        mvc.get("/api/v1/car-checkups/performed/last-ten").andExpect { status { isForbidden() } }
        mvc.get("/api/v1/car-checkups/upcoming").andExpect { status { isForbidden() } }
        mvc.delete("/api/v1/car-checkups/1").andExpect { status { isForbidden() } }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should delete car check-up when good car check-up id")
    fun test10() {
        mvc.delete("/api/v1/car-checkups/1") .andExpect { status { isAccepted() } }
        mvc.delete("/api/v1/car-checkups/2") .andExpect { status { isAccepted() } }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should throw 404 not found for delete request when bad car check-up id")
    fun test11() {
        mvc.delete("/api/v1/car-checkups/65") .andExpect { status { isNotFound() } }
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_ADMIN"])
    @DisplayName("should return upcoming car check-ups of specified interval when called")
    fun test12() {
        val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val time1 = LocalDateTime.now().plusMonths(5).truncatedTo(ChronoUnit.MILLIS)
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3.copy(timeOfCheckUp = time1))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/4") }
        }
        val time2 = LocalDateTime.now().plusDays(25)
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3.copy(timeOfCheckUp = time2))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/5") }
        }
        mvc.get("/api/v1/car-checkups/upcoming?duration=HALF_YEAR").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
                        {
                            "_embedded": {
                                "item": [
                                    {
                                        "id": 4,
                                        "timeOfCheckUp": "${time1.format(dateTimeFormat)}",
                                        "workerName": "Adam",
                                        "price": 45.97,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/4"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    },
                                    {
                                        "id": 5,
                                        "timeOfCheckUp": "${time2.format(dateTimeFormat)}",
                                        "workerName": "Adam",
                                        "price": 45.97,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/5"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    }
                                ]
                            }
                        }
                    """.trimIndent()
                )
            }
        }
        mvc.get("/api/v1/car-checkups/upcoming").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
                        {
                            "_embedded": {
                                "item": [
                                    {
                                        "id": 5,
                                        "timeOfCheckUp": "${time2.format(dateTimeFormat)}",
                                        "workerName": "Adam",
                                        "price": 45.97,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/5"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    }
                                ]
                            }
                        }
                    """.trimIndent()
                )
            }
        }
        mvc.get("/api/v1/car-checkups/upcoming?duration=MONTH").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
                        {
                            "_embedded": {
                                "item": [
                                    {
                                        "id": 5,
                                        "timeOfCheckUp": "${time2.format(dateTimeFormat)}",
                                        "workerName": "Adam",
                                        "price": 45.97,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/5"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    }
                                ]
                            }
                        }
                    """.trimIndent()
                )
            }
        }
        val time3 = LocalDateTime.now().plusDays(4)
        mvc.post("/api/v1/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3.copy(timeOfCheckUp = time3))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/api/v1/car-checkups/6") }
        }
        mvc.get("/api/v1/car-checkups/upcoming?duration=WEEK").andExpect {
            status { is2xxSuccessful() }
            content {
                json(
                    """
                        {
                            "_embedded": {
                                "item": [
                                    {
                                        "id": 6,
                                        "timeOfCheckUp": "${time3.format(dateTimeFormat)}",
                                        "workerName": "Adam",
                                        "price": 45.97,
                                        "_links": {
                                            "self": {
                                                "href": "http://localhost/api/v1/car-checkups/6"
                                            },
                                            "car": {
                                                "href": "http://localhost/api/v1/cars/1"
                                            }
                                        }
                                    }
                                ]
                            }
                        }
                    """.trimIndent()
                )
            }
        }
    }

}