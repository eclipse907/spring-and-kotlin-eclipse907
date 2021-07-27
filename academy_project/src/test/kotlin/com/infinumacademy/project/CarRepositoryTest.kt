package com.infinumacademy.project

import com.infinumacademy.project.repositories.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest @Autowired constructor(
    private val carRepository: CarRepository
) {

    @Test
    fun test1() {
        val car = TestData.carToAdd1.toCar()
        assertThat(carRepository.save(car)).isEqualTo(car.copy(id = 1))
    }

    @Test
    fun test2() {
        val car = TestData.carToAdd1.toCar()
        assertThat(carRepository.save(car)).isEqualTo(car.copy(id = 2))
        assertThat(carRepository.findById(2)).isEqualTo(car.copy(id = 2))
    }

    @Test
    fun test3() {
        val car1 = TestData.carToAdd1.toCar()
        assertThat(carRepository.save(car1)).isEqualTo(car1.copy(id = 3))
        val car2 = TestData.carToAdd2.toCar()
        assertThat(carRepository.save(car2)).isEqualTo(car2.copy(id = 4))
        val car3 = TestData.carToAdd3.toCar()
        assertThat(carRepository.save(car3)).isEqualTo(car3.copy(id = 5))
        assertThat(carRepository.findAll()).isEqualTo(listOf(car1.copy(id = 3), car2.copy(id = 4), car3.copy(id = 5)))
    }

}