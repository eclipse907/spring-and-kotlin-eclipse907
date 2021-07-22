package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.Car
import java.time.LocalDate
import java.time.Year

data class CarRequestDto(
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Year,
    val serialNumber: Long
) {
    fun toCar() = Car(
        0,
        ownerId,
        dateAdded,
        manufacturerName,
        modelName,
        productionYear,
        serialNumber,
        mutableListOf()
    )
}
