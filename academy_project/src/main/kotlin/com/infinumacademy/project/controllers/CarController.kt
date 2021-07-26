package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.CarRequestDto
import com.infinumacademy.project.services.CarService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/cars")
class CarController(private val carService: CarService) {

    @PostMapping
    fun addCar(@RequestBody carToAdd: CarRequestDto): ResponseEntity<Unit> {
        val createdCar = carService.addCar(carToAdd.toCar())
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCar.id).toUri()
        ).build()
    }

    @GetMapping("/{id}")
    fun getCar(@PathVariable("id") id: Long) = ResponseEntity.ok(carService.getCarWithId(id))

    @GetMapping
    fun getAllCars() = ResponseEntity.ok(carService.getAllCars())

}