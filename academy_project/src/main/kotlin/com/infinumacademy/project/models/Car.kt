package com.infinumacademy.project.models

import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "car")
data class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotNull(message = "Owner id can't be null")
    val ownerId: Long,

    @NotNull(message = "Date added can't be null")
    val dateAdded: LocalDate,

    @NotBlank(message = "Manufacturer name can't be blank")
    val manufacturerName: String,

    @NotBlank(message = "Model name can't be blank")
    val modelName: String,

    @NotNull(message = "Production year can't be null")
    val productionYear: Int,

    @Column(unique = true)
    @NotNull(message = "Serial number can't be null")
    val serialNumber: Long
)