package courseservice

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CourseServiceTest {

    private val courseRepository = mockk<CourseRepository>()

    private lateinit var courseService: CourseService

    @BeforeEach
    fun setUp() {
        courseService = CourseService(courseRepository)
    }

    @Test
    @DisplayName("Test add course")
    fun test1() {
        every {
            courseRepository.insert("OOP")
        } returns 1
        every {
            courseRepository.insert("OPP")
        } returns 2
        every {
            courseRepository.findById(1)
        } returns Course(1, "OOP")
        every {
            courseRepository.findById(2)
        } returns Course(2, "OPP")
        assertThat(courseService.addCourse("OOP")).isEqualTo(1)
        assertThat(courseService.addCourse("OPP")).isEqualTo(2)
        assertThat(courseService.getCourseWithId(1)).isEqualTo(Course(1, "OOP"))
        assertThat(courseService.getCourseWithId(2)).isEqualTo(Course(2, "OPP"))
    }

    @Test
    @DisplayName("Test get course with id")
    fun test2() {
        every {
            courseRepository.insert("OOP")
        } returns 1
        every {
            courseRepository.insert("OPP")
        } returns 2
        every {
            courseRepository.findById(1)
        } returns Course(1, "OOP")
        every {
            courseRepository.findById(2)
        } returns Course(2, "OPP")
        assertThat(courseService.addCourse("OOP")).isEqualTo(1)
        assertThat(courseService.addCourse("OPP")).isEqualTo(2)
        assertThat(courseService.getCourseWithId(1)).isEqualTo(Course(1, "OOP"))
        assertThat(courseService.getCourseWithId(2)).isEqualTo(Course(2, "OPP"))
    }

    @Test
    @DisplayName("Test delete course with id")
    fun test3() {
        every {
            courseRepository.insert("OOP")
        } returns 1
        every {
            courseRepository.insert("OPP")
        } returns 2
        every {
            courseRepository.insert("APR")
        } returns 3
        every {
            courseRepository.findById(1)
        } returns Course(1, "OOP")
        every {
            courseRepository.findById(2)
        } returns Course(2, "OPP")
        every {
            courseService.deleteCourseWithId(1)
        } returns Course(1, "OOP")
        every {
            courseService.deleteCourseWithId(2)
        } returns Course(2, "OPP")
        every {
            courseService.deleteCourseWithId(3)
        } returns Course(3, "APR")
        assertThat(courseService.addCourse("OOP")).isEqualTo(1)
        assertThat(courseService.addCourse("OPP")).isEqualTo(2)
        assertThat(courseService.addCourse("APR")).isEqualTo(3)
        assertThat(courseService.deleteCourseWithId(1)).isEqualTo(Course(1, "OOP"))
        assertThat(courseService.deleteCourseWithId(3)).isEqualTo(Course(3, "APR"))
        assertThat(courseService.getCourseWithId(2)).isEqualTo(Course(2, "OPP"))
    }

}