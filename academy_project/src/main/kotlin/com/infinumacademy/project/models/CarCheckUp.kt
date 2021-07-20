package com.infinumacademy.project.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CarCheckUp(
    val id: Long,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Zagreb")
    val timeOfCheckUp: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
)
