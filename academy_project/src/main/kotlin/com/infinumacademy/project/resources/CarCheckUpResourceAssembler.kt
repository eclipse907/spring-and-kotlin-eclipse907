package com.infinumacademy.project.resources

import com.infinumacademy.project.annotations.ResourceAssembler
import com.infinumacademy.project.controllers.CarCheckUpController
import com.infinumacademy.project.controllers.CarController
import com.infinumacademy.project.dtos.CarCheckUpDto
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo

@ResourceAssembler
class CarCheckUpResourceAssembler : RepresentationModelAssemblerSupport<CarCheckUpDto, CarCheckUpResource>(
    CarCheckUpController::class.java,
    CarCheckUpResource::class.java
) {

    override fun toModel(entity: CarCheckUpDto) = createModelWithId(entity.id, entity).apply {
        add(linkTo<CarController> {
            getCar(entity.car.id)
        }.withRel("car"))
    }

    override fun instantiateModel(entity: CarCheckUpDto) = CarCheckUpResource(
        entity.id,
        entity.timeOfCheckUp,
        entity.workerName,
        entity.price
    )

}