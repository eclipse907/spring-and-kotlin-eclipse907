package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import java.time.LocalDate

data class CarWithCarCheckUpsDto(
    val id: Long,
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Int,
    val serialNumber: Long,
    val carCheckUps: List<CarCheckUpDto>
) {
    constructor(car: Car, carCheckUps: List<CarCheckUp>) : this(
        car.id,
        car.ownerId,
        car.dateAdded,
        car.manufacturerName,
        car.modelName,
        car.productionYear,
        car.serialNumber,
        carCheckUps.map { CarCheckUpDto(it) }
    )
}
