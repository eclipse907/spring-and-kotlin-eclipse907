package com.infinumacademy.project.controllers

import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/cars/{carId}/car-check-ups")
class CarCheckUpsForCarController(private val carCheckUpService: CarCheckUpService) {

    @GetMapping
    fun getAllCarCheckUpsWithCarId(@PathVariable("carId") carId: Long, pageable: Pageable) =
        ResponseEntity.ok(carCheckUpService.getAllCarCheckUpsWithCarId(carId, pageable))

}