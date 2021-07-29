package com.infinumacademy.project.models

import java.time.LocalDate
import javax.persistence.*
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

    @ManyToOne(optional = false)
    val carModel: CarModel,

    @NotNull(message = "Production year can't be null")
    val productionYear: Short,

    @Column(unique = true)
    @NotNull(message = "Serial number can't be null")
    val serialNumber: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Car

        return id > 0 && id == other.id
    }

    override fun hashCode() = javaClass.hashCode()
}