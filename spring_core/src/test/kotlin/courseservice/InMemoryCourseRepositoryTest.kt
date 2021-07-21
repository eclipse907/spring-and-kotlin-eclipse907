package courseservice

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class InMemoryCourseRepositoryTest {

    private val dataSource = mockk<DataSource>()

    private lateinit var inMemoryCourseRepository: InMemoryCourseRepository

    @BeforeEach
    fun setUp() {
        inMemoryCourseRepository = InMemoryCourseRepository(dataSource)
    }

    @Test
    @DisplayName("Test insert")
    fun test1() {
        assertThat(inMemoryCourseRepository.insert("OOP")).isEqualTo(1)
        assertThat(inMemoryCourseRepository.insert("OPP")).isEqualTo(2)
        assertThat(inMemoryCourseRepository.findById(1)).isEqualTo(Course(1, "OOP"))
        assertThat(inMemoryCourseRepository.findById(2)).isEqualTo(Course(2, "OPP"))
    }

    @Test
    @DisplayName("Test find by id")
    fun test2() {
        assertThat(inMemoryCourseRepository.insert("OOP")).isEqualTo(1)
        assertThat(inMemoryCourseRepository.insert("OPP")).isEqualTo(2)
        assertThat(inMemoryCourseRepository.findById(1)).isEqualTo(Course(1, "OOP"))
        assertThat(inMemoryCourseRepository.findById(2)).isEqualTo(Course(2, "OPP"))
        assertThatThrownBy {
            inMemoryCourseRepository.findById(54353534534)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 54353534534 not found")
    }

    @Test
    @DisplayName("Test delete by id")
    fun test3() {
        assertThat(inMemoryCourseRepository.insert("OOP")).isEqualTo(1)
        assertThat(inMemoryCourseRepository.insert("OPP")).isEqualTo(2)
        assertThat(inMemoryCourseRepository.insert("Apr")).isEqualTo(3)
        assertThat(inMemoryCourseRepository.deleteById(1)).isEqualTo(Course(1, "OOP"))
        assertThatThrownBy {
            inMemoryCourseRepository.deleteById(1)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 1 not found")
        assertThat(inMemoryCourseRepository.deleteById(3)).isEqualTo(Course(3, "Apr"))
        assertThatThrownBy {
            inMemoryCourseRepository.deleteById(3)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 3 not found")
        assertThat(inMemoryCourseRepository.findById(2)).isEqualTo(Course(2, "OPP"))
    }

}
