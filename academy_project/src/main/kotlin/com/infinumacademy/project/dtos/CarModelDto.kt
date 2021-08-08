package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.CarModel

data class CarModelDto(
    val manufacturer: String,
    val modelName: String
) {
    constructor(carModel: CarModel): this (
        manufacturer = carModel.manufacturer,
        modelName = carModel.modelName
    )
}
