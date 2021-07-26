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
import javax.sql.DataSource

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
class JdbcCarRepositoryTest @Autowired constructor(
    jdbcTemplate: NamedParameterJdbcTemplate,
    dataSource: DataSource
) {

    private val jdbcCarRepository = JdbcCarRepository(jdbcTemplate, dataSource)

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
            ownerId = 45,
            dateAdded = LocalDate.parse("2020-02-01"),
            manufacturerName = "Toyota",
            modelName = "Yaris",
            productionYear = Year.parse("2018"),
            serialNumber = 123456
        )
        val car2 = Car(
            ownerId = 56,
            dateAdded = LocalDate.parse("2019-09-03"),
            manufacturerName = "Opel",
            modelName = "Astra",
            productionYear = Year.parse("2016"),
            serialNumber = 123654
        )
        assertThat(jdbcCarRepository.save(car1)).isEqualTo(1)
        assertThat(jdbcCarRepository.save(car2)).isEqualTo(2)
        assertThat(jdbcCarRepository.findById(1)).isEqualTo(car1.copy(id = 1))
        assertThat(jdbcCarRepository.findById(2)).isEqualTo(car2.copy(id = 2))
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