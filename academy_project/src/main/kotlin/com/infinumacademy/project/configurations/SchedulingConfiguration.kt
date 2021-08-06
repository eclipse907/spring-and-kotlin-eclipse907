package com.infinumacademy.project.configurations

import com.infinumacademy.project.properties.UpdateCarModelsSchedulingProperties
import com.infinumacademy.project.services.CarModelService
import net.javacrumbs.shedlock.core.LockProvider
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import javax.sql.DataSource

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@EnableScheduling
@EnableConfigurationProperties(UpdateCarModelsSchedulingProperties::class)
@ConditionalOnProperty("update-car-models.scheduling.enabled", havingValue = "true")
class SchedulingConfiguration(private val carModelService: CarModelService) {

    @Scheduled(fixedDelayString = "\${update-car-models.delay}")
    @SchedulerLock(name = "updateCarModels", lockAtLeastFor = "1m")
    fun updateCarModels() = carModelService.updateCarModels()

    @Bean
    fun lockProvider(dataSource: DataSource): LockProvider {
        return JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(JdbcTemplate(dataSource))
                .usingDbTime()
                .build()
        )
    }

}