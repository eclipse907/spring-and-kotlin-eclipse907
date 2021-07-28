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

@SpringBootTest
@AutoConfigureMockMvc
class CarCheckUpsForCarControllerTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    @Test
    fun test1() {
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
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd2)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/2") }
        }
        mvc.post("/car-checkups") {
            content = mapper.writeValueAsString(TestData.carCheckUpToAdd3)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/car-checkups/3") }
        }
        mvc.get("/cars/1/car-check-ups").andExpect {
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