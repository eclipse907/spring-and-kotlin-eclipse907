package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.Car

interface CarRepository {
    fun findById(id: Long): Car?
    fun save(car: Car): Long
    fun findAll(): List<Car>
}
