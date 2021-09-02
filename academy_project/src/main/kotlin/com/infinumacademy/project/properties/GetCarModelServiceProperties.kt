package com.infinumacademy.project.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "get-car-model-service")
@ConstructorBinding
data class GetCarModelServiceProperties(
    val baseUrl: String
)
