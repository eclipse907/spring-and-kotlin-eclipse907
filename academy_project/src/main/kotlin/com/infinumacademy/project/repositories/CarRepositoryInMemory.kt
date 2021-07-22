package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import org.springframework.stereotype.Repository

@Repository
class CarRepositoryInMemory {

    private val cars = mutableMapOf<Long, Car>()

    fun findById(id: Long) = cars[id]

    fun save(car: Car) {
        cars[car.id] = car
    }

    fun updateCarWithCarCheckUp(carCheckUp: CarCheckUp): Unit? {
        cars[carCheckUp.carId]?.carCheckUps?.add(carCheckUp) ?: return null
        return Unit
    }

    fun findAll() = cars.values.toList()

    fun getAllCarIds() = cars.keys.toList()

}
