package com.infinumacademy.project.services

import com.infinumacademy.project.annotations.RestService
import com.infinumacademy.project.dtos.GetCarModelsResponseDto
import com.infinumacademy.project.exceptions.NoCarModelsRetrievedException
import com.infinumacademy.project.repositories.CarModelRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@RestService
class HttpCarModelService(
    private val webClient: WebClient,
    private val carModelRepository: CarModelRepository
) {

    private fun getAllCarModels() = webClient
        .get()
        .uri("/api/v1/cars")
        .retrieve()
        .bodyToMono<GetCarModelsResponseDto>()
        .map { it.data }
        .block()

    @Transactional
    @CacheEvict("car_model", allEntries = true)
    fun updateCarModels() {
        val carModelsToAdd = getAllCarModels()
            ?: throw NoCarModelsRetrievedException("Error while retrieving car models from http server")
        carModelsToAdd.forEach {
            carModelRepository.findByManufacturerAndModelName(it.manufacturer, it.modelName)?.let { carModel ->
                carModelRepository.save(
                    carModel.copy(
                        manufacturer = it.manufacturer,
                        modelName = it.modelName,
                        isCommon = it.isCommon
                    )
                )
            } ?: carModelRepository.save(it.toCarModel())
        }
    }

}