package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.AddCarCheckUpDto
import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@Controller
@RequestMapping("/car-checkups")
class CarCheckUpController(private val carCheckUpService: CarCheckUpService) {

    @PostMapping
    fun addCarCheckUp(@Valid @RequestBody carCheckUpToAdd: AddCarCheckUpDto): ResponseEntity<Unit> {
        val createdCheckUp = carCheckUpService.addCarCheckUp(carCheckUpToAdd)
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCheckUp.id).toUri()
        ).build()
    }

    @GetMapping("/{id}")
    fun getCarCheckUp(@PathVariable("id") id: Long) = ResponseEntity.ok(carCheckUpService.getCarCheckUpWithId(id))

    @GetMapping
    fun getAllCarCheckUps() = ResponseEntity.ok(carCheckUpService.getAllCarCheckUps())

    @GetMapping("/car-id/{car-id}")
    fun getAllCarCheckUpsWithCarId(@PathVariable("car-id") carId: Long, pageable: Pageable) =
        ResponseEntity.ok(carCheckUpService.getAllCarCheckUpsWithCarId(carId, pageable))
}