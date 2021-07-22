package com.infinumacademy.project.controllers

import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
class CarCheckUpController(private val carCheckUpService: CarCheckUpService) {

    @PostMapping("/car-checkups")
    fun addCarCheckUp(@RequestBody carCheckUp: CarCheckUp): ResponseEntity<Unit> {
        val createdCheckUp = carCheckUpService.addCarCheckUp(carCheckUp)
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCheckUp.id).toUri()
        ).build()
    }

    @GetMapping("/car-checkups/{id}")
    fun getCarCheckUp(@PathVariable id: Long) = ResponseEntity.ok(carCheckUpService.getCarCheckUpWithId(id))

    @GetMapping("/car-checkups")
    fun getAllCarCheckUps() = ResponseEntity.ok(carCheckUpService.getAllCarCheckUps())

}