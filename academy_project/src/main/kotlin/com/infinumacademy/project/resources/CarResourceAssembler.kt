package com.infinumacademy.project.resources

import com.infinumacademy.project.annotations.ResourceAssembler
import com.infinumacademy.project.controllers.CarCheckUpsForCarController
import com.infinumacademy.project.controllers.CarController
import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.dtos.CarDto
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport
import org.springframework.hateoas.server.mvc.linkTo

@ResourceAssembler
class CarResourceAssembler :
    RepresentationModelAssemblerSupport<CarDto, CarResource>(CarController::class.java, CarResource::class.java) {

    private val noPagination = Pageable.unpaged()
    private val nullAssembler = PagedResourcesAssembler<CarCheckUpDto>(null, null)

    override fun toModel(entity: CarDto) = createModelWithId(entity.id, entity).apply {
        add(linkTo<CarCheckUpsForCarController> {
            getAllCarCheckUpsForCarWithId(
                entity.id,
                noPagination,
                nullAssembler
            )
        }.withRel("check-ups"))
    }

    override fun instantiateModel(entity: CarDto) = CarResource(
        entity.id,
        entity.ownerId,
        entity.dateAdded,
        entity.manufacturerName,
        entity.modelName,
        entity.productionYear,
        entity.serialNumber
    )

}