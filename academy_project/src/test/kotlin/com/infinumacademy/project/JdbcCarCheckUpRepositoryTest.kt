package com.infinumacademy.project

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.JdbcCarCheckUpRepository
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
import java.time.LocalDateTime
import java.time.Year

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
class JdbcCarCheckUpRepositoryTest @Autowired constructor(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val jdbcCarCheckUpRepository = JdbcCarCheckUpRepository(jdbcTemplate)

    private val jdbcCarRepository = JdbcCarRepository(jdbcTemplate)

    @Test
    fun test1() {
        jdbcCarRepository.save(
            Car(
                0,
                45,
                LocalDate.parse("2020-02-01"),
                "Toyota",
                "Yaris",
                Year.parse("2018"),
                123456
            )
        )
        jdbcCarRepository.save(
            Car(
                0,
                45,
                LocalDate.parse("2020-02-01"),
                "Toyota",
                "Yaris",
                Year.parse("2018"),
                123456
            )
        )
        assertThatThrownBy {
            jdbcCarCheckUpRepository.findById(12112)
        }.isInstanceOf(IncorrectResultSizeDataAccessException::class.java)
        assertThat(jdbcCarCheckUpRepository.findAll()).isEqualTo(listOf<CarCheckUp>())
        assertThat(jdbcCarCheckUpRepository.findByCarId(6756)).isEqualTo(listOf<CarCheckUp>())
    }

    @Test
    fun test2() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("2019-12-23T10:47:49"),
            "Alice",
            150.34,
            2
        )
        jdbcCarCheckUpRepository.save(carCheckUp1)
        jdbcCarCheckUpRepository.save(carCheckUp2)
        assertThat(jdbcCarCheckUpRepository.findById(carCheckUp1.id)).isEqualTo(carCheckUp1)
        assertThat(jdbcCarCheckUpRepository.findById(carCheckUp2.id)).isEqualTo(carCheckUp2)
    }

    @Test
    fun test3() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUp2 = CarCheckUp(
            1,
            LocalDateTime.parse("2019-12-23T10:47:49"),
            "Alice",
            150.34,
            2
        )
        assertThat(jdbcCarCheckUpRepository.findAll()).isEqualTo(listOf(carCheckUp1, carCheckUp2))
    }

    @Test
    fun test4() {
        val carCheckUp1 = CarCheckUp(
            0,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUp3 = CarCheckUp(
            2,
            LocalDateTime.parse("2014-04-13T17:23:20"),
            "John",
            15.34,
            1
        )
        jdbcCarCheckUpRepository.save(carCheckUp3)
        assertThat(jdbcCarCheckUpRepository.findByCarId(1)).isEqualTo(listOf(carCheckUp1, carCheckUp3))
    }

}