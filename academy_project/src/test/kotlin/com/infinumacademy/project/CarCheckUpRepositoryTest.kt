package com.infinumacademy.project

import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarModelRepository
import com.infinumacademy.project.repositories.CarRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarCheckUpRepositoryTest @Autowired constructor(
    private val carCheckUpRepository: CarCheckUpRepository,
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
        assertThat(carRepository.save(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        })).isEqualTo(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 1)
        }.copy(id = 1))
        val carCheckUp = TestData.carCheckUpToAdd1.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 1)
            }.copy(id = 1)
        }
        assertThat(carCheckUpRepository.save(carCheckUp)).isEqualTo(carCheckUp.copy(id = 1))
    }

    @Test
    fun test2() {
        assertThat(carRepository.save(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 4)
        })).isEqualTo(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 4)
        }.copy(id = 2))
        val carCheckUp = TestData.carCheckUpToAdd1.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 4)
            }.copy(id = 2)
        }
        assertThat(carCheckUpRepository.save(carCheckUp)).isEqualTo(carCheckUp.copy(id = 2))
        assertThat(carCheckUpRepository.findById(2)).isEqualTo(carCheckUp.copy(id = 2))
        assertThat(carCheckUpRepository.findById(45)).isNull()
    }

    @Test
    fun test3() {
        assertThat(carRepository.save(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 7)
        })).isEqualTo(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 7)
        }.copy(id = 3))
        val carCheckUp2 = TestData.carCheckUpToAdd2.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 7)
            }.copy(id = 3)
        }
        assertThat(carCheckUpRepository.save(carCheckUp2)).isEqualTo(carCheckUp2.copy(id = 3))
        val carCheckUp1 = TestData.carCheckUpToAdd1.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 7)
            }.copy(id = 3)
        }
        assertThat(carCheckUpRepository.save(carCheckUp1)).isEqualTo(carCheckUp1.copy(id = 4))
        val carCheckUp3 = TestData.carCheckUpToAdd3.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 7)
            }.copy(id = 3)
        }
        assertThat(carCheckUpRepository.save(carCheckUp3)).isEqualTo(carCheckUp3.copy(id = 5))
        assertThat(carCheckUpRepository.findAllByOrderByTimeOfCheckUpDesc(Pageable.unpaged())).isEqualTo(
            PageImpl(listOf(carCheckUp1.copy(id = 4), carCheckUp2.copy(id = 3), carCheckUp3.copy(id = 5)))
        )
    }

    @Test
    fun test4() {
        assertThat(carRepository.save(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 10)
        })).isEqualTo(TestData.carToAdd1.toCar { _, _ ->
            TestData.carModelToAdd1.toCarModel().copy(id = 10)
        }.copy(id = 4))
        assertThat(carRepository.save(TestData.carToAdd2.toCar { _, _ ->
            TestData.carModelToAdd2.toCarModel().copy(id = 11)
        })).isEqualTo(TestData.carToAdd2.toCar { _, _ ->
            TestData.carModelToAdd2.toCarModel().copy(id = 11)
        }.copy(id = 5))
        val carCheckUp2 = TestData.carCheckUpToAdd2.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 10)
            }.copy(id = 4)
        }
        assertThat(carCheckUpRepository.save(carCheckUp2)).isEqualTo(carCheckUp2.copy(id = 6))
        val carCheckUp1 = TestData.carCheckUpToAdd1.toCarCheckUp {
            TestData.carToAdd1.toCar { _, _ ->
                TestData.carModelToAdd1.toCarModel().copy(id = 10)
            }.copy(id = 4)
        }
        assertThat(carCheckUpRepository.save(carCheckUp1)).isEqualTo(carCheckUp1.copy(id = 7))
        val carCheckUp3 = TestData.carCheckUpToAdd3.toCarCheckUp {
            TestData.carToAdd2.toCar { _, _ ->
                TestData.carModelToAdd2.toCarModel().copy(id = 11)
            }.copy(id = 5)
        }
        assertThat(carCheckUpRepository.save(carCheckUp3)).isEqualTo(carCheckUp3.copy(id = 8))
        assertThat(carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(4)).isEqualTo(
            listOf(carCheckUp1.copy(id = 7), carCheckUp2.copy(id = 6))
        )
        assertThat(carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(5)).isEqualTo(
            listOf(carCheckUp3.copy(id = 8))
        )
    }

}