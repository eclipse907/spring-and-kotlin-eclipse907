package courseservice

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(ApplicationConfiguration::class)
@TestPropertySource("classpath:test-infile.properties")
class CourseServiceWithInFileRepositoryIntegrationTest @Autowired constructor(
    private val applicationContext: ApplicationContext,
    private val courseService: CourseService
) {

    @Test
    @DisplayName("All beans initialized")
    fun test1() {
        Assertions.assertThat(
            applicationContext
        ).isNotNull
        Assertions.assertThat(
            courseService
        ).isNotNull
    }

    @Test
    @DisplayName("Adding of courses")
    fun test2() {
        val id1 = courseService.addCourse("OOP")
        Assertions.assertThat(courseService.getCourseWithId(id1)).isEqualTo(Course(id1, "OOP"))
        val id2 = courseService.addCourse("OPP")
        Assertions.assertThat(courseService.getCourseWithId(id2)).isEqualTo(Course(id2, "OPP"))
    }

    @Test
    @DisplayName("Retrieving courses with nonexistent id throws exception")
    fun test3() {
        Assertions.assertThatThrownBy {
            courseService.getCourseWithId(28934239874)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 28934239874 not found")
        Assertions.assertThatThrownBy {
            courseService.getCourseWithId(4535453545334)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 4535453545334 not found")
    }

    @Test
    @DisplayName("Deleting of courses")
    fun test4() {
        Assertions.assertThat(courseService.getCourseWithId(1)).isEqualTo(Course(1, "OOP"))
        Assertions.assertThat(courseService.getCourseWithId(2)).isEqualTo(Course(2, "OPP"))
        val id3 = courseService.addCourse("APR")
        Assertions.assertThat(courseService.getCourseWithId(id3)).isEqualTo(Course(id3, "APR"))

        courseService.deleteCourseWithId(1)
        Assertions.assertThatThrownBy {
            courseService.getCourseWithId(1)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 1 not found")
        courseService.deleteCourseWithId(3)
        Assertions.assertThatThrownBy {
            courseService.getCourseWithId(3)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 3 not found")
        Assertions.assertThat(courseService.getCourseWithId(2)).isEqualTo(Course(2, "OPP"))
    }

    @Test
    @DisplayName("Deleting courses with nonexistent id throws exception")
    fun test5() {
        Assertions.assertThatThrownBy {
            courseService.deleteCourseWithId(5353434534)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 5353434534 not found")
        Assertions.assertThatThrownBy {
            courseService.deleteCourseWithId(87464684)
        }.isInstanceOf(CourseNotFoundException::class.java)
            .hasMessage("Course with and ID 87464684 not found")
    }

    @Test
    @DisplayName("Adding of courses creates good ids")
    fun test6() {
        val id4 = courseService.addCourse("NENR")
        Assertions.assertThat(courseService.getCourseWithId(id4)).isEqualTo(Course(3, "NENR"))
        val id5 = courseService.addCourse("DU")
        Assertions.assertThat(courseService.getCourseWithId(id5)).isEqualTo(Course(4, "DU"))
    }

    @Test
    @DisplayName("DataSource values")
    fun test7() {
        val dataSource = applicationContext.getBean<DataSource>()
        Assertions.assertThat(dataSource)
            .isEqualTo(DataSource("test-database", "test-admin", "test-admin"))
    }

}