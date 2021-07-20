package com.infinumacademy.project

import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CarCheckUpRepositoryTest {

    private val carCheckUpRepository = CarCheckUpRepository()

    private val dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

    @Test
    fun test() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("06-06-2021 20:35:10", dateTimeFormat),
            "Bob",
            23.56,
            0
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("23-12-2019 10:47:49", dateTimeFormat),
            "Alice",
            150.34,
            0
        )
        carCheckUpRepository.insert(carCheckUp1)
        assertThat(carCheckUpRepository.findById(0)).isEqualTo(carCheckUp1)
        carCheckUpRepository.insert(carCheckUp2)
        assertThat(carCheckUpRepository.findById(1)).isEqualTo(carCheckUp2)
        assertThat(carCheckUpRepository.getAllCarCheckUps()).isEqualTo(listOf(carCheckUp1, carCheckUp2))
    }
}
