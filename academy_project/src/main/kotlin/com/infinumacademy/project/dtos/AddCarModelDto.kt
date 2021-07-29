package com.infinumacademy.project.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import com.infinumacademy.project.models.CarModel
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddCarModelDto(
    @field:NotBlank(message = "Manufacturer can't be blank")
    val manufacturer: String,

    @field:NotBlank(message = "Model name can't be blank")
    @JsonProperty("model_name")
    val modelName: String,

    @field:NotNull(message = "Is common can't be null")
    @field:Min(0)
    @field:Max(1)
    @JsonProperty("is_common")
    val isCommon: Short
) {
    fun toCarModel() = CarModel(
        manufacturer = manufacturer,
        modelName = modelName,
        isCommon = isCommon
    )
}