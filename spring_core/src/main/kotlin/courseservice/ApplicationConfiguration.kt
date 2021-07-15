package courseservice

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.PropertySource
import org.springframework.core.io.PathResource

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
class ApplicationConfiguration(
    private val dataSource: DataSource,
) {

    @Bean
    @Lazy(true)
    fun getCourseFileResource() = PathResource("${dataSource.dbName}.txt")

}
