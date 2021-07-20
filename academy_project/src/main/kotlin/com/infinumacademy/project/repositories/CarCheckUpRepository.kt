package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp
import org.springframework.stereotype.Repository

@Repository
class CarCheckUpRepository {

    private val carCheckUps = mutableMapOf<Long, CarCheckUp>()

    fun findById(id: Long) = carCheckUps[id]

    fun insert(carCheckUp: CarCheckUp) {
        carCheckUps[carCheckUp.id] = carCheckUp
    }

    fun getAllCarCheckUps() = carCheckUps.values.toList()

}
