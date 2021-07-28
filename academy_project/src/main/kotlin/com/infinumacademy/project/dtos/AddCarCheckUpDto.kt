package com.infinumacademy.project.dtos

import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

data class AddCarCheckUpDto(
    @field:NotNull(message = "Time of check-up can't be null")
    val timeOfCheckUp: LocalDateTime,

    @field:NotBlank(message = "Worker name can't be blank")
    val workerName: String,

    @field:NotNull(message = "Price can't be null")
    @field:PositiveOrZero(message = "Price can't be less than zero")
    val price: Double,

    val carId: Long
) {
    fun toCarCheckUp(carFetcher: (Long) -> Car) = CarCheckUp(
        timeOfCheckUp = timeOfCheckUp,
        workerName = workerName,
        price = price,
        car = carFetcher.invoke(carId)
    )
}