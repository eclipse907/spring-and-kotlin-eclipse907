package courseservice

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.core.io.PathResource

class InFileCourseRepositoryTest {

    private val coursesFile = PathResource("test-database.txt")
    private lateinit var inFileCourseRepository: InFileCourseRepository

    @BeforeEach
    fun setUp() {
        inFileCourseRepository = InFileCourseRepository(coursesFile)
    }

    @Test
    @DisplayName("Test insert")
    fun test1() {
        assertThat(inFileCourseRepository.insert("OOP")).isEqualTo(1)
        assertThat(inFileCourseRepository.insert("OPP")).isEqualTo(2)
        assertThat(inFileCourseRepository.findById(1)).isEqualTo(Course(1, "OOP"))
        assertThat(inFileCourseRepository.findById(2)).isEqualTo(Course(2, "OPP"))
    }

    @Test
    @DisplayName("Test find by id")
    fun test2() {
        assertThat(inFileCourseRepository.findById(1)).isEqualTo(Course(1, "OOP"))
        assertThat(inFileCourseRepository.findById(2)).isEqualTo(Course(2, "OPP"))
        assertThatThrownBy {
            inFileCourseRepository.findById(54353534534)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 54353534534 not found")
    }

    @Test
    @DisplayName("Test delete by id")
    fun test3() {
        assertThat(inFileCourseRepository.insert("Apr")).isEqualTo(3)
        assertThat(inFileCourseRepository.deleteById(1)).isEqualTo(Course(1, "OOP"))
        assertThatThrownBy {
            inFileCourseRepository.deleteById(1)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 1 not found")
        assertThat(inFileCourseRepository.deleteById(3)).isEqualTo(Course(3, "Apr"))
        assertThatThrownBy {
            inFileCourseRepository.deleteById(3)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 3 not found")
        assertThat(inFileCourseRepository.findById(2)).isEqualTo(Course(2, "OPP"))
    }

}
