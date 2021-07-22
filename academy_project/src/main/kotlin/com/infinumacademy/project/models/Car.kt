package com.infinumacademy.project.models

import java.time.LocalDate
import java.time.Year

data class Car(
    var id: Long,
    val ownerId: Long,
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Year,
    val serialNumber: Long,
    val carCheckUps: MutableList<CarCheckUp> = mutableListOf()
)