package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarModel
import org.springframework.data.repository.Repository

interface CarModelRepository : Repository<CarModel, Long> {
    fun saveAll(carModels: Iterable<CarModel>): Iterable<CarModel>
    fun findByManufacturerAndModelName(manufacturer: String, modelName: String): CarModel?
    fun findById(id: Long): CarModel?
}