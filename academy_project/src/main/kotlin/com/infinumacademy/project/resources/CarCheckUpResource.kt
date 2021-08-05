package com.infinumacademy.project.resources

import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
data class CarCheckUpResource(
    val id: Long,
    val timeOfCheckUp: LocalDateTime,
    val workerName: String,
    val price: Double
) : RepresentationModel<CarCheckUpResource>()
