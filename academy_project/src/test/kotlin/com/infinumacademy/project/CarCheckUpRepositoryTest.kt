package com.infinumacademy.project

import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CarCheckUpRepositoryTest {

    private val carCheckUpRepository = CarCheckUpRepository()

    @Test
    fun test() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            0
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("2019-12-23T10:47:49"),
            "Alice",
            150.34,
            0
        )
        carCheckUpRepository.save(carCheckUp1)
        assertThat(carCheckUpRepository.findById(0)).isEqualTo(carCheckUp1)
        carCheckUpRepository.save(carCheckUp2)
        assertThat(carCheckUpRepository.findById(1)).isEqualTo(carCheckUp2)
        assertThat(carCheckUpRepository.findAll()).isEqualTo(listOf(carCheckUp1, carCheckUp2))
    }
}
