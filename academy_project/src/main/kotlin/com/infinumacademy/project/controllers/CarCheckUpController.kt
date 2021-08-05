package com.infinumacademy.project.controllers

import com.infinumacademy.project.dtos.AddCarCheckUpDto
import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.dtos.UpcomingCarCheckUpsInterval
import com.infinumacademy.project.resources.CarCheckUpResourceAssembler
import com.infinumacademy.project.services.CarCheckUpService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun addCarCheckUp(@Valid @RequestBody carCheckUpToAdd: AddCarCheckUpDto): ResponseEntity<Unit> {
        val createdCheckUp = carCheckUpService.addCarCheckUp(carCheckUpToAdd)
        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdCheckUp.id).toUri()
        ).build()
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun getCarCheckUp(@PathVariable("id") id: Long) =
        ResponseEntity.ok(resourceAssembler.toModel(carCheckUpService.getCarCheckUpWithId(id)))

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun getAllCarCheckUps(pageable: Pageable, pagedResourcesAssembler: PagedResourcesAssembler<CarCheckUpDto>) =
        ResponseEntity.ok(
            pagedResourcesAssembler.toModel(
                carCheckUpService.getAllCarCheckUps(pageable),
                resourceAssembler
            )
        )

    @GetMapping("/performed/last-ten")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun getLastTenCarCheckUpsPerformed() =
        ResponseEntity.ok(resourceAssembler.toCollectionModel(carCheckUpService.getLastTenCarCheckUpsPerformed()))

    @GetMapping("/upcoming")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun getUpcomingCarCheckUps(@RequestParam("duration") duration: UpcomingCarCheckUpsInterval?) =
        ResponseEntity.ok(
            resourceAssembler.toCollectionModel(
                carCheckUpService.getUpcomingCarCheckUps(
                    duration ?: UpcomingCarCheckUpsInterval.MONTH
                )
            )
        )

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    fun deleteCarCheckUp(@PathVariable("id") id: Long) = ResponseEntity.accepted().build<Unit>().also {
        carCheckUpService.deleteCarCheckUpWithId(id)
    }

}