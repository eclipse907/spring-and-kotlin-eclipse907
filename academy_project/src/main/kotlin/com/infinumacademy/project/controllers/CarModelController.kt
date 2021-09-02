package com.infinumacademy.project.controllers

import com.infinumacademy.project.resources.CarModelResource
import com.infinumacademy.project.resources.CarModelResourceAssembler
import com.infinumacademy.project.services.CarModelService
import org.springframework.hateoas.CollectionModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("api/v1/car-models")
class CarModelController(
    private val carModelService: CarModelService,
    private val resourceAssembler: CarModelResourceAssembler
) {

    @GetMapping
    fun getAllCarModelsOfStoredCars(): ResponseEntity<CollectionModel<CarModelResource>> =
        ResponseEntity.ok(resourceAssembler.toCollectionModel(carModelService.getAllCarModelsForCarsInDatabase()))

}