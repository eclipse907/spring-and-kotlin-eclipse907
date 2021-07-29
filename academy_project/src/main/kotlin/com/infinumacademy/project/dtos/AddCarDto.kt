package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarModel
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AddCarDto(
    @field:NotNull(message = "Owner id can't be null")
    val ownerId: Long,

    @field:NotNull(message = "Date added can't be null")
    val dateAdded: LocalDate,

    @field:NotBlank(message = "Manufacturer name can't be blank")
    val manufacturerName: String,

    @field:NotBlank(message = "Model name can't be blank")
    val modelName: String,

    @field:NotNull(message = "Production year can't be null")
    val productionYear: Short,

    @field:NotNull(message = "Serial number can't be null")
    val serialNumber: Long
) {
    fun toCar(carModelFetcher: (String, String) -> CarModel) = Car(
        ownerId = ownerId,
        dateAdded = dateAdded,
        carModel = carModelFetcher.invoke(manufacturerName, modelName),
        productionYear = productionYear,
        serialNumber = serialNumber
    )
}
