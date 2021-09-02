package com.infinumacademy.project.services

import com.infinumacademy.project.annotations.RestService
import com.infinumacademy.project.dtos.AddCarModelDto
import com.infinumacademy.project.dtos.GetCarModelsResponseDto
import com.infinumacademy.project.exceptions.NoCarModelsRetrievedException
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@RestService
class HttpGetCarModelsService(private val webClient: WebClient) : GetCarModelsService {

    override fun getAllCarModels(): List<AddCarModelDto> = webClient
        .get()
        .uri("/api/v1/cars")
        .retrieve()
        .bodyToMono<GetCarModelsResponseDto>()
        .map { it.data }
        .block()
        ?.map { AddCarModelDto(it.manufacturer, it.modelName, it.isCommon) }
        ?: throw NoCarModelsRetrievedException("Error while retrieving car models from http server")

}