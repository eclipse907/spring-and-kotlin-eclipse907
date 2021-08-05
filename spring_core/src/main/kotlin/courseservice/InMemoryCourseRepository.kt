package courseservice

import org.springframework.context.annotation.Conditional
import org.springframework.stereotype.Component

@Component
@Conditional(CourseRepositoryInMemoryTypeCondition::class)
class InMemoryCourseRepository(
    private val dataSource: DataSource
) : CourseRepository {

    init {
        println(dataSource)
    }

    private val savedCourses: MutableMap<Long, Course> = mutableMapOf()

    override fun insert(name: String): Long {
        val newId = (savedCourses.keys.maxOrNull() ?: 0) + 1
        return newId.also { savedCourses[newId] = Course(newId, name) }
    }

    override fun findById(id: Long) = savedCourses[id] ?: throw CourseNotFoundException(id)

    override fun deleteById(id: Long) = savedCourses.remove(id) ?: throw CourseNotFoundException(id)

}
