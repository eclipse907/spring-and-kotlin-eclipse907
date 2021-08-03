package com.infinumacademy.project.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class GetCarModelsDataDto(
    @field:NotBlank(message = "Manufacturer can't be blank")
    val manufacturer: String,

    @field:NotBlank(message = "Model name can't be blank")
    @JsonProperty("model_name")
    val modelName: String,

    @field:NotNull(message = "Is common can't be null")
    @JsonProperty("is_common")
    val isCommon: Boolean
)