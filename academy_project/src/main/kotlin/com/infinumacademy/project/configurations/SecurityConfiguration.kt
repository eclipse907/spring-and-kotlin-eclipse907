package com.infinumacademy.project.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            cors { }
            csrf { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            authorizeRequests {
                authorize(HttpMethod.POST, "api/v1/car-checkups", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "api/v1/car-checkups/{id}", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "api/v1/car-checkups", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "api/v1/car-checkups/performed/last-ten", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "api/v1/car-checkups/upcoming", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.DELETE, "api/v1/car-checkups/{id}", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "api/v1/cars/{carId}/car-check-ups", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.POST, "api/v1/cars", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.GET, "api/v1/cars/{id}", hasAnyAuthority("SCOPE_ADMIN", "SCOPE_USER"))
                authorize(HttpMethod.GET, "api/v1/cars", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.DELETE, "api/v1/cars/{id}", hasAuthority("SCOPE_ADMIN"))
                authorize(HttpMethod.GET, "api/v1/car-models", permitAll)
            }
            oauth2ResourceServer {
                jwt {}
            }
        }
        return http.build()
    }

}