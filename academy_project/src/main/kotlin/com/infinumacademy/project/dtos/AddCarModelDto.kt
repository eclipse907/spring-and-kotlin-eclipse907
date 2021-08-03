package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.CarModel

data class AddCarModelDto(
    val manufacturer: String,
    val modelName: String,
    val isCommon: Boolean
) {
    fun toCarModel() = CarModel(
        manufacturer = manufacturer,
        modelName = modelName,
        isCommon = isCommon
    )
}
