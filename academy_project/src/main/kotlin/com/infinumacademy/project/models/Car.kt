package com.infinumacademy.project.models

import com.fasterxml.jackson.annotation.*
import java.time.LocalDate
import java.time.Year

@JsonIgnoreProperties(value = ["id", "carCheckUps"], allowGetters = true)
data class Car(
    var id: Long? = null,
    val ownerId: Long,
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "Europe/Zagreb")
    val dateAdded: LocalDate,
    val manufacturerName: String,
    val modelName: String,
    val productionYear: Year,
    val serialNumber: Long,
    val carCheckUps: MutableList<CarCheckUp> = mutableListOf()
) {
    fun addCarCheckUp(carCheckUp: CarCheckUp) {
        when (carCheckUp.carId) {
            id -> carCheckUps.add(carCheckUp)
            else -> throw IllegalArgumentException("Car check-up car id does not match this car id")
        }
    }
}
