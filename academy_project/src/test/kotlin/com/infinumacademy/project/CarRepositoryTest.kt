package com.infinumacademy.project

import com.infinumacademy.project.repositories.CarModelRepository
import com.infinumacademy.project.repositories.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarRepositoryTest @Autowired constructor(
    private val carRepository: CarRepository,
    private val carModelRepository: CarModelRepository
) {

    @BeforeEach
    fun setUp() {
        carModelRepository.saveAll(
            listOf(
                TestData.carModelToAdd1.toCarModel(),
                TestData.carModelToAdd2.toCarModel(),
                TestData.carModelToAdd3.toCarModel()
            )
        )
    }

    @Test
    fun test1() {
        val car = TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }
        assertThat(carRepository.save(car)).isEqualTo(car.copy(id = 1))
    }

    @Test
    fun test2() {
        val car = TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 4)
        }
        assertThat(carRepository.save(car)).isEqualTo(car.copy(id = 2))
        assertThat(carRepository.findById(2)).isEqualTo(car.copy(id = 2))
    }

    @Test
    fun test3() {
        val car1 = TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 7)
        }
        assertThat(carRepository.save(car1)).isEqualTo(car1.copy(id = 3))
        val car2 = TestData.carToAdd2.toCar { _, _ ->
            TestData.carModelToAdd2.toCarModel().copy(id = 8)
        }
        assertThat(carRepository.save(car2)).isEqualTo(car2.copy(id = 4))
        val car3 = TestData.carToAdd3.toCar { _, _ ->
            TestData.carModelToAdd3.toCarModel().copy(id = 9)
        }
        assertThat(carRepository.save(car3)).isEqualTo(car3.copy(id = 5))
        assertThat(carRepository.findAll(Pageable.unpaged())).isEqualTo(
            listOf(
                car1.copy(id = 3),
                car2.copy(id = 4),
                car3.copy(id = 5)
            )
        )
    }

}