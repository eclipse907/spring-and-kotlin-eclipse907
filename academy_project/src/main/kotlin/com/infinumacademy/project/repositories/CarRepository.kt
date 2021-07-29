package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.Car
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository

interface CarRepository : Repository<Car, Long> {
    fun findById(id: Long): Car?
    fun save(car: Car): Car
    fun findAll(): List<Car>
    fun findAll(pageable: Pageable): Page<Car>
    fun findBySerialNumber(serialNumber: Long): Car?
}
