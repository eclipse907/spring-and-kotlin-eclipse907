package com.infinumacademy.project.controllers

import com.infinumacademy.project.models.Car
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
    fun addCar(@RequestBody car: Car): ResponseEntity<Unit> {
        val createdCar = carService.addCar(car)
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCar.id).toUri()
        ).build()
    }

    @GetMapping("/cars/{id}")
    fun getCar(@PathVariable id: Long) = ResponseEntity.ok(carService.getCarWithId(id))

    @GetMapping("/cars")
    fun getAllCars() = ResponseEntity.ok(carService.getAllCars())

}