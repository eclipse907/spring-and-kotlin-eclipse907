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
import javax.sql.DataSource

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
class JdbcCarCheckUpRepositoryTest @Autowired constructor(
    jdbcTemplate: NamedParameterJdbcTemplate,
    dataSource: DataSource
) {

    private val jdbcCarCheckUpRepository = JdbcCarCheckUpRepository(jdbcTemplate, dataSource)

    private val jdbcCarRepository = JdbcCarRepository(jdbcTemplate, dataSource)

    @Test
    fun test1() {
        jdbcCarRepository.save(
            Car(
                ownerId = 45,
                dateAdded = LocalDate.parse("2020-02-01"),
                manufacturerName = "Toyota",
                modelName = "Yaris",
                productionYear = Year.parse("2018"),
                serialNumber = 123456
            )
        )
        jdbcCarRepository.save(
            Car(
                ownerId = 45,
                dateAdded = LocalDate.parse("2020-02-01"),
                manufacturerName = "Toyota",
                modelName = "Yaris",
                productionYear = Year.parse("2018"),
                serialNumber = 654321
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
            timeOfCheckUp = LocalDateTime.parse("2021-06-06T20:35:10"),
            workerName = "Bob",
            price = 23.56,
            carId = 1
        )
        val carCheckUp2 = CarCheckUp(
            timeOfCheckUp = LocalDateTime.parse("2019-12-23T10:47:49"),
            workerName = "Alice",
            price = 150.34,
            carId = 2
        )
        assertThat(jdbcCarCheckUpRepository.save(carCheckUp1)).isEqualTo(1)
        assertThat(jdbcCarCheckUpRepository.save(carCheckUp2)).isEqualTo(2)
        assertThat(jdbcCarCheckUpRepository.findById(1)).isEqualTo(carCheckUp1.copy(id = 1))
        assertThat(jdbcCarCheckUpRepository.findById(2)).isEqualTo(carCheckUp2.copy(id = 2))
    }

    @Test
    fun test3() {
        val carCheckUp1 = CarCheckUp(
            1,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUp2 = CarCheckUp(
            2,
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
            1,
            LocalDateTime.parse("2021-06-06T20:35:10"),
            "Bob",
            23.56,
            1
        )
        val carCheckUp3 = CarCheckUp(
            timeOfCheckUp = LocalDateTime.parse("2014-04-13T17:23:20"),
            workerName = "John",
            price = 15.34,
            carId = 1
        )
        assertThat(jdbcCarCheckUpRepository.save(carCheckUp3)).isEqualTo(3)
        assertThat(jdbcCarCheckUpRepository.findByCarId(1)).isEqualTo(listOf(carCheckUp1, carCheckUp3.copy(id = 3)))
    }

}