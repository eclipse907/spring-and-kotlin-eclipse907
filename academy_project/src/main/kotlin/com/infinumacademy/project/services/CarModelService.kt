package com.infinumacademy.project.services

import com.infinumacademy.project.repositories.CarModelRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CarModelService(
    private val getCarModelsService: GetCarModelsService,
    private val carModelRepository: CarModelRepository
) {

    @Transactional
    @CacheEvict("car_model", allEntries = true)
    fun updateCarModels() = getCarModelsService.getAllCarModels().filter {
        carModelRepository.findByManufacturerAndModelName(it.manufacturer, it.modelName)?.let { false } ?: true
    }.map { it.toCarModel() }.run { carModelRepository.saveAll(this) }

    @Cacheable("car_model")
    fun getCarModelWithManufacturerAndModelName(manufacturer: String, modelName: String) =
        carModelRepository.findByManufacturerAndModelName(manufacturer, modelName)

}