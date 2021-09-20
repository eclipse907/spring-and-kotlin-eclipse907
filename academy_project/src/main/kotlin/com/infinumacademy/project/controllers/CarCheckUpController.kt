package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.AddCarCheckUpDto
import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.dtos.UpcomingCarCheckUpsInterval
import com.infinumacademy.project.resources.CarCheckUpResource
import com.infinumacademy.project.resources.CarCheckUpResourceAssembler
import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@Controller
@RequestMapping("api/v1/car-checkups")
class CarCheckUpController(
    private val carCheckUpService: CarCheckUpService,
    private val resourceAssembler: CarCheckUpResourceAssembler
) {

    @PostMapping
    fun addCarCheckUp(@Valid @RequestBody carCheckUpToAdd: AddCarCheckUpDto): ResponseEntity<Unit> {
        val createdCheckUp = carCheckUpService.addCarCheckUp(carCheckUpToAdd)
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCheckUp.id).toUri()
        ).build()
    }

    @GetMapping("/{id}")

    fun getCarCheckUp(@PathVariable("id") id: Long): ResponseEntity<CarCheckUpResource> =
        ResponseEntity.ok(resourceAssembler.toModel(carCheckUpService.getCarCheckUpWithId(id)))

    @GetMapping
    fun getAllCarCheckUps(
        pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUpDto>
    ): ResponseEntity<PagedModel<CarCheckUpResource>> =
        ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                carCheckUpService.getAllCarCheckUps(pageable),
                resourceAssembler
            )
        )

    @GetMapping("/performed/last-ten")
    fun getLastTenCarCheckUpsPerformed(): ResponseEntity<CollectionModel<CarCheckUpResource>> =
        ResponseEntity.ok(resourceAssembler.toCollectionModel(carCheckUpService.getLastTenCarCheckUpsPerformed()))

    @GetMapping("/upcoming")
    fun getUpcomingCarCheckUps(@RequestParam("duration") duration: UpcomingCarCheckUpsInterval?): ResponseEntity<CollectionModel<CarCheckUpResource>> =
        ResponseEntity.ok(
            resourceAssembler.toCollectionModel(
                carCheckUpService.getUpcomingCarCheckUps(
                    duration ?: UpcomingCarCheckUpsInterval.MONTH
                )
            )
        )

    @DeleteMapping("/{id}")
    fun deleteCarCheckUp(@PathVariable("id") id: Long): ResponseEntity<Unit> =
        ResponseEntity.accepted().build<Unit>().also {
            carCheckUpService.deleteCarCheckUpWithId(id)
        }

}