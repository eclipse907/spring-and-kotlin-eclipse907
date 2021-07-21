package com.infinumacademy.project.repositories

import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import org.springframework.stereotype.Repository

@Repository
class CarRepository {

    private val cars = mutableMapOf<Long, Car>()

    fun findById(id: Long) = cars[id]

    fun save(car: Car) {
        cars[car.id ?: throw NullPointerException("Car id is null")] = car
    }

    fun updateCarWithCarCheckUp(carCheckUp: CarCheckUp) = cars[carCheckUp.carId]?.addCarCheckUp(carCheckUp)

    fun findAll() = cars.values.toList()

    fun getAllCarIds() = cars.keys.toList()

}
