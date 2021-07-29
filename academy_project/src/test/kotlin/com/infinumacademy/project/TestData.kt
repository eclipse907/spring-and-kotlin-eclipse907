package com.infinumacademy.project

import com.infinumacademy.project.dtos.AddCarCheckUpDto
import com.infinumacademy.project.dtos.AddCarDto
import com.infinumacademy.project.dtos.AddCarModelDto
import com.infinumacademy.project.models.CarCheckUp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year

object TestData {
    val carToAdd1 = AddCarDto(
        45,
        LocalDate.parse("2020-02-01"),
        "Toyota",
        "Yaris",
        2018,
        123456
    )
    val carToAdd2 = AddCarDto(
        56,
        LocalDate.parse("2019-09-03"),
        "Opel",
        "Astra",
        2016,
        654321
    )
    val carToAdd3 = AddCarDto(
        78,
        LocalDate.parse("2021-02-01"),
        "Toyota",
        "Corolla",
        2020,
        654123
    )
    val carCheckUpToAdd1 = AddCarCheckUpDto(
        LocalDateTime.parse("2021-06-06T20:35:10"),
        "Bob",
        23.56,
        1
    )
    val carCheckUpToAdd2 = AddCarCheckUpDto(
        LocalDateTime.parse("2018-12-23T10:30:10"),
        "Tom",
        57.34,
        1
    )
    val carCheckUpToAdd3 = AddCarCheckUpDto(
        LocalDateTime.parse("2016-08-06T15:05:30"),
        "Adam",
        45.97,
        1
    )
    val carModelToAdd1 = AddCarModelDto(
        "Toyota",
        "Yaris",
        0
    )
    val carModelToAdd2 = AddCarModelDto(
        "Opel",
        "Astra",
        0
    )
    val carModelToAdd3 = AddCarModelDto(
        "Toyota",
        "Corolla",
        0
    )
}