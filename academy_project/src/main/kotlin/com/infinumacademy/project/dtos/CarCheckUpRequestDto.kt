package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.CarCheckUp
import java.time.LocalDateTime

data class CarCheckUpRequestDto(
    val timeOfCheckUp: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
) {
    fun toCarCheckUp() = CarCheckUp(
        timeOfCheckUp = timeOfCheckUp,
        workerName = workerName,
        price = price,
        carId = carId
    )
}