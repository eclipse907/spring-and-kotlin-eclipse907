package com.infinumacademy.project.models

import java.time.LocalDateTime

data class CarCheckUp(
    val id: Long,
    val timeOfCheckUp: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
)
