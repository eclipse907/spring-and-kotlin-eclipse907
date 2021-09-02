package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.AddCarDto
import com.infinumacademy.project.dtos.CarDto
import com.infinumacademy.project.resources.CarResource
import com.infinumacademy.project.resources.CarResourceAssembler
import com.infinumacademy.project.services.CarService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@Controller
@RequestMapping("api/v1/cars")
class CarController(
    private val carService: CarService,
    private val resourceAssembler: CarResourceAssembler
) {

    @PostMapping
    fun addCar(@Valid @RequestBody carToAdd: AddCarDto): ResponseEntity<Unit> {
        val createdCar = carService.addCar(carToAdd)
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCar.id).toUri()
        ).build()
    }

    @GetMapping("/{id}")
    fun getCar(@PathVariable("id") id: Long): ResponseEntity<CarResource> =
        ResponseEntity.ok(resourceAssembler.toModel(carService.getCarWithId(id)))

    @GetMapping
    fun getAllCars(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarDto>
    ): ResponseEntity<PagedModel<CarResource>> =
        ResponseEntity.ok(pagedResourcesAssembler.toModel(carService.getAllCars(pageable), resourceAssembler))

    @DeleteMapping("/{id}")
    fun deleteCar(@PathVariable("id") id: Long): ResponseEntity<Unit> = ResponseEntity.ok().build<Unit>().also {
        carService.deleteCarWithId(id)
    }

}