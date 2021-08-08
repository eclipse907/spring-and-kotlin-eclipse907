package com.infinumacademy.project.resources

import com.infinumacademy.project.annotations.ResourceAssembler
import com.infinumacademy.project.controllers.CarModelController
import com.infinumacademy.project.dtos.CarModelDto
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport

@ResourceAssembler
class CarModelResourceAssembler : RepresentationModelAssemblerSupport<CarModelDto, CarModelResource>(
    CarModelController::class.java,
    CarModelResource::class.java
) {

    override fun toModel(entity: CarModelDto) = instantiateModel(entity)

    override fun instantiateModel(entity: CarModelDto) = CarModelResource(
        entity.manufacturer,
        entity.modelName
    )

}