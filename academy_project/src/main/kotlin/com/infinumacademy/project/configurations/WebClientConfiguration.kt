package com.infinumacademy.project.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration(
    @Value("\${car-model-service.base-url}") val baseUrl: String
) {

    @Bean
    fun webClient(webClientBuilder: WebClient.Builder) = webClientBuilder.baseUrl(baseUrl).build()

}