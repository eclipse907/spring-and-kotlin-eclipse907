package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.Car
import java.time.LocalDate

data class CarDto(
    val id: Long,
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: Long
) {
    constructor(car: Car): this(
        car.id,
        car.ownerId,
        car.dateAdded,
        car.manufacturerName,
        car.modelName,
        car.productionYear,
        car.serialNumber
    )
}