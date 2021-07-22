package com.infinumacademy.project

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.repositories.JdbcCarRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.annotation.Commit
import java.time.LocalDate
import java.time.Year

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
class JdbcCarRepositoryTest @Autowired constructor(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val jdbcCarRepository = JdbcCarRepository(jdbcTemplate)

    @Test
    fun test1() {
        assertThatThrownBy {
            jdbcCarRepository.findById(12112)
        }.isInstanceOf(IncorrectResultSizeDataAccessException::class.java)
        assertThat(jdbcCarRepository.findAll()).isEqualTo(listOf<Car>())
    }

    @Test
    fun test2() {
        val car1 = Car(
            0,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        val car2 = Car(
            0,
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654
        )
        assertThat(jdbcCarRepository.save(car1)).isEqualTo(1)
        assertThat(jdbcCarRepository.save(car2)).isEqualTo(2)
        car1.id = 1
        car2.id = 2
        assertThat(jdbcCarRepository.findById(car1.id)).isEqualTo(car1)
        assertThat(jdbcCarRepository.findById(car2.id)).isEqualTo(car2)
    }

    @Test
    fun test3() {
        val car1 = Car(
            1,
            45,
            LocalDate.parse("2020-02-01"),
            "Toyota",
            "Yaris",
            Year.parse("2018"),
            123456
        )
        val car2 = Car(
            2,
            56,
            LocalDate.parse("2019-09-03"),
            "Opel",
            "Astra",
            Year.parse("2016"),
            123654
        )
        assertThat(jdbcCarRepository.findAll()).isEqualTo(listOf(car1, car2))
    }

}