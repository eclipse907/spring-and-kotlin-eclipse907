package com.infinumacademy.project.resources

import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = IanaLinkRelations.ITEM_VALUE)
class CarModelResource(
    val manufacturer: String,
    val modelName: String
) : RepresentationModel<CarModelResource>()
