package com.infinumacademy.project.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CarCheckUp(
    val id: Long,
    @JsonFormat(timezone = JsonFormat.DEFAULT_TIMEZONE)
    val timeOfCheckUp: LocalDateTime,
    val workerName: String,
    val price: Double,
    val carId: Long
)
