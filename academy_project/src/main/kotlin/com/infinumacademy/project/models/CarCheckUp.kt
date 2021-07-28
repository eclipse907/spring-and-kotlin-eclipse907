package com.infinumacademy.project.models

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "car_check_up")
data class CarCheckUp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotNull(message = "Time of check-up can't be null")
    val timeOfCheckUp: LocalDateTime,

    @NotBlank(message = "Worker name can't be blank")
    val workerName: String,

    @NotNull(message = "Price can't be null")
    @PositiveOrZero(message = "Price can't be less than zero")
    val price: Double,

    @ManyToOne(optional = false)
    val car: Car
)
