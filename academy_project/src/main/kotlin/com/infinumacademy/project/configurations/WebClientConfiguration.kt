package com.infinumacademy.project.configurations

import com.infinumacademy.project.properties.GetCarModelServiceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(GetCarModelServiceProperties::class)
class WebClientConfiguration {

    @Bean
    fun webClient(webClientBuilder: WebClient.Builder, properties: GetCarModelServiceProperties) =
        webClientBuilder.baseUrl(properties.baseUrl).build()

}