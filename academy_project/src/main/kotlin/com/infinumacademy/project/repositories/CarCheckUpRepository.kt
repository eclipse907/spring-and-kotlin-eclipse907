package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp

interface CarCheckUpRepository {
    fun findById(id: Long): CarCheckUp?
    fun save(carCheckUp: CarCheckUp)
    fun findByCarId(carId: Long): List<CarCheckUp>
    fun findAll(): List<CarCheckUp>
}
