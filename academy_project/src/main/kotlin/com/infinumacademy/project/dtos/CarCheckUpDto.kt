package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.CarCheckUp
import java.time.LocalDateTime

data class CarCheckUpDto(
    val id: Long,
    val timeOfCheckUp: LocalDateTime,
    val workerName: String,
    val price: Double,
    val car: CarDto
) {
    constructor(carCheckUp: CarCheckUp): this(
        carCheckUp.id,
        carCheckUp.timeOfCheckUp,
        carCheckUp.workerName,
        carCheckUp.price,
        CarDto(carCheckUp.car)
    )
}
