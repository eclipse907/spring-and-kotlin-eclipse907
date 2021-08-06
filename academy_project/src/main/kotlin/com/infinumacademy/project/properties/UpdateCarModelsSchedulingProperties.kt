package com.infinumacademy.project.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "update-car-models.scheduling")
data class UpdateCarModelsSchedulingProperties(
    val delay: Duration
)
