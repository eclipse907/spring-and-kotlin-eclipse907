package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.repositories.CarModelRepository
import com.infinumacademy.project.resources.CarCheckUpResourceAssembler
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
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
    private val carModelRepository: CarModelRepository,
    private val resourceAssembler: CarCheckUpResourceAssembler
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
        mvc.get("/api/v1/cars/1/car-check-ups").andExpect {
            status { is2xxSuccessful() }
            content {
                jsonPath("$._embedded.item") {
                    value(
                        mapper.writeValueAsString(
                            resourceAssembler.toCollectionModel(
                                listOf(
                                    CarCheckUpDto(TestData.carCheckUpToAdd1.toCarCheckUp {
                                        TestData.carToAdd1.toCar { _, _ ->
                                            TestData.carModelToAdd1.toCarModel().copy(id = 1)
                                        }.copy(id = 1)
                                    }.copy(id = 1)),
                                    CarCheckUpDto(TestData.carCheckUpToAdd2.toCarCheckUp {
                                        TestData.carToAdd1.toCar { _, _ ->
                                            TestData.carModelToAdd1.toCarModel().copy(id = 1)
                                        }.copy(id = 1)
                                    }.copy(id = 2)),
                                    CarCheckUpDto(TestData.carCheckUpToAdd3.toCarCheckUp {
                                        TestData.carToAdd1.toCar { _, _ ->
                                            TestData.carModelToAdd1.toCarModel().copy(id = 1)
                                        }.copy(id = 1)
                                    }.copy(id = 3))
                                )
                            )
                        )
                    )
                }
                jsonPath("$.totalElements") { value(3) }
            }
        }
    }

}