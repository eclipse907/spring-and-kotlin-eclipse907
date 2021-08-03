package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.CarDto
import com.infinumacademy.project.repositories.CarModelRepository
import com.infinumacademy.project.resources.CarResourceAssembler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper,
    private val carModelRepository: CarModelRepository,
    private val resourceAssembler: CarResourceAssembler
) {

    @Test
    fun test1() {
        carModelRepository.saveAll(
            listOf(
                TestData.carModelToAdd1.toCarModel(),
                TestData.carModelToAdd2.toCarModel(),
                TestData.carModelToAdd3.toCarModel()
            )
        )
        mvc.get("/api/v1/cars/0").andExpect {
            status { isNotFound() }
            jsonPath("$.message") { value("404 NOT_FOUND \"Car with id 0 not found\"") }
        }
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
    fun test2() {
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
                    mapper.writeValueAsString(
                        resourceAssembler.toModel(CarDto(TestData.carToAdd1.toCar { _, _ ->
                            TestData.carModelToAdd1.toCarModel().copy(id = 1)
                        }.copy(id = 1), listOf()))
                    )
                )
            }
        }
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
    fun test3() {
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
                    mapper.writeValueAsString(
                        resourceAssembler.toCollectionModel(
                            listOf(
                                CarDto(TestData.carToAdd1.toCar { _, _ ->
                                    TestData.carModelToAdd1.toCarModel().copy(id = 1)
                                }.copy(id = 1)),
                                CarDto(TestData.carToAdd2.toCar { _, _ ->
                                    TestData.carModelToAdd2.toCarModel().copy(id = 1)
                                }.copy(id = 3)),
                                CarDto(TestData.carToAdd3.toCar { _, _ ->
                                    TestData.carModelToAdd3.toCarModel().copy(id = 1)
                                }.copy(id = 4))
                            )
                        )
                    )
                )
            }
        }
    }

    @Test
    fun test4() {
        mvc.get("/api/v1/cars").andExpect {
            status { is2xxSuccessful() }
            content {
                jsonPath("$._embedded.item") {
                    value(
                        mapper.writeValueAsString(
                            resourceAssembler.toCollectionModel(
                                listOf(
                                    CarDto(TestData.carToAdd1.toCar { _, _ ->
                                        TestData.carModelToAdd1.toCarModel().copy(id = 1)
                                    }.copy(id = 1)),
                                    CarDto(TestData.carToAdd2.toCar { _, _ ->
                                        TestData.carModelToAdd2.toCarModel().copy(id = 1)
                                    }.copy(id = 3)),
                                    CarDto(TestData.carToAdd3.toCar { _, _ ->
                                        TestData.carModelToAdd3.toCarModel().copy(id = 1)
                                    }.copy(id = 4))
                                )
                            )
                        )
                    )
                }
                jsonPath("$.page.totalElements") { value(3) }
            }
        }
    }

}