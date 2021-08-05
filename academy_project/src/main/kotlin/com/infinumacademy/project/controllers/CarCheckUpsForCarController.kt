package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.resources.CarCheckUpResourceAssembler
import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("api/v1/cars/{carId}/car-check-ups")
class CarCheckUpsForCarController(
    private val carCheckUpService: CarCheckUpService,
    private val resourcesAssembler: CarCheckUpResourceAssembler
) {

    @GetMapping
    fun getAllCarCheckUpsWithCarId(
        @PathVariable("carId") carId: Long,
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUpDto>
    ) =
        ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                carCheckUpService.getAllCarCheckUpsWithCarId(carId, pageable),
                resourcesAssembler
            )
        )

}