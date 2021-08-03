package com.infinumacademy.project.services

import com.infinumacademy.project.dtos.AddCarModelDto

interface GetCarModelsService {

    fun getAllCarModels(): List<AddCarModelDto>

}