package courseservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class DataSource(
    @Value("\${course-repository.database.name}") val dbName: String,
    @Value("\${course-repository.database.user}") val username: String,
    @Value("\${course-repository.database.password}") val password: String
)
