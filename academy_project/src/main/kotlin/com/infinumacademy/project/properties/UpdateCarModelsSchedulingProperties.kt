package com.infinumacademy.project.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConfigurationProperties(prefix = "update-car-models.scheduling")
@ConstructorBinding
data class UpdateCarModelsSchedulingProperties(
    val delay: Duration,
    val enabled: Boolean
)
