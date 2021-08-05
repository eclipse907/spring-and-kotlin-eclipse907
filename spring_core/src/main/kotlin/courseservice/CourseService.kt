package courseservice

import org.springframework.stereotype.Component

@Component
class CourseService(
    private val courseRepository: CourseRepository
) {

    fun addCourse(name: String) = courseRepository.insert(name)

    fun getCourseWithId(id: Long) = courseRepository.findById(id)

    fun deleteCourseWithId(id: Long) = courseRepository.deleteById(id)

}
