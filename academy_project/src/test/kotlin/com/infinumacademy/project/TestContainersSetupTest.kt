package com.infinumacademy.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.infinumacademy.project.dtos.AddCarDto
import org.junit.jupiter.api.Test
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.springtest.MockServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDate
import java.time.Year

@MockServerTest
@SpringBootTest
@AutoConfigureMockMvc
class TestContainersSetupTest @Autowired constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {

    lateinit var mockServerClient: MockServerClient

    @Test
    fun test() {
        mvc.post("/cars") {
            content = mapper.writeValueAsString(TestData.carToAdd1)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated() }
            header { stringValues("Location", "http://localhost/cars/1") }
        }
    }

}