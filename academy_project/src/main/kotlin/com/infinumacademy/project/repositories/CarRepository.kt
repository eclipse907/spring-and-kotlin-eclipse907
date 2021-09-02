package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface CarRepository : Repository<Car, Long> {
    fun findById(id: Long): Car?
    fun save(car: Car): Car
    fun findAll(pageable: Pageable): Page<Car>
    fun findBySerialNumber(serialNumber: Long): Car?
    fun deleteById(id: Long)
    @Query("select distinct car.carModel from Car as car")
    fun findAllDistinctCarModels(): List<CarModel>
}
