package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.CarRequestDto
import com.infinumacademy.project.services.CarService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
class CarController(private val carService: CarService) {

    @PostMapping("/cars")
    fun addCar(@RequestBody carToAdd: CarRequestDto): ResponseEntity<Unit> {
        val createdCar = carService.addCar(carToAdd.toCar())
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCar.id).toUri()
        ).build()
    }

    @GetMapping("/cars/{id}")
    fun getCar(@PathVariable id: Long) = ResponseEntity.ok(carService.getCarWithId(id))

    @GetMapping("/cars")
    fun getAllCars() = ResponseEntity.ok(carService.getAllCars())

}