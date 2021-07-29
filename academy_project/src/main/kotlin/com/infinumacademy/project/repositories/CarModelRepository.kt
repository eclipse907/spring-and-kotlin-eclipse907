package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarModel
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.Repository

interface CarModelRepository : Repository<CarModel, Long> {
    fun save(carModel: CarModel): CarModel
    @Cacheable("car_model")
    fun findByManufacturerAndModelName(manufacturer: String, modelName: String): CarModel?
}