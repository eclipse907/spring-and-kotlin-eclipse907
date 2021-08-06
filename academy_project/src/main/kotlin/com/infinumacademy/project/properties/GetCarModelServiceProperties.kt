package com.infinumacademy.project.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "get-car-model-service")
data class GetCarModelServiceProperties(
    val baseUrl: String
)
