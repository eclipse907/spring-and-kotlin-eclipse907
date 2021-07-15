package courseservice

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

class CourseRepositoryInMemoryTypeCondition : Condition {

    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata) =
        context.environment.getProperty("course-repository.database.type") == "in memory"

}