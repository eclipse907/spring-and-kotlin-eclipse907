package com.infinumacademy.project.models

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "car_model")
data class CarModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank(message = "Manufacturer can't be blank")
    val manufacturer: String,

    @NotBlank(message = "Model name can't be blank")
    val modelName: String,

    @NotNull(message = "Is common can't be null")
    val isCommon: Boolean
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CarModel

        return id > 0 && id == other.id
    }

    override fun hashCode() = javaClass.hashCode()
}
