package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp
import org.springframework.stereotype.Repository

@Repository
class CarCheckUpRepositoryInMemory {

    private val carCheckUps = mutableMapOf<Long, CarCheckUp>()

    fun findById(id: Long) = carCheckUps[id]

    fun save(carCheckUp: CarCheckUp) {
        carCheckUps[carCheckUp.id] = carCheckUp
    }

    fun findAll() = carCheckUps.values.toList()

}
