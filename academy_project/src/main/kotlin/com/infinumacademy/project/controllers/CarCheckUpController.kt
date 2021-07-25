package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.CarCheckUpRequestDto
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Controller
@RequestMapping("/car-checkups")
class CarCheckUpController(private val carCheckUpService: CarCheckUpService) {

    @PostMapping
    fun addCarCheckUp(@RequestBody carCheckUpToAdd: CarCheckUpRequestDto): ResponseEntity<Unit> {
        val createdCheckUp = carCheckUpService.addCarCheckUp(carCheckUpToAdd.toCarCheckUp())
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCheckUp.id).toUri()
        ).build()
    }

    @GetMapping("/{id}")
    fun getCarCheckUp(@PathVariable("id") id: Long) = ResponseEntity.ok(carCheckUpService.getCarCheckUpWithId(id))

    @GetMapping
    fun getAllCarCheckUps() = ResponseEntity.ok(carCheckUpService.getAllCarCheckUps())

}