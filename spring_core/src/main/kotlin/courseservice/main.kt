package courseservice

import org.springframework.beans.factory.getBean
import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun main() {
    val applicationContext = AnnotationConfigApplicationContext(ApplicationConfiguration::class.java)
    val courseService = applicationContext.getBean<CourseService>()
    val id1 = courseService.addCourse("OOP")
    val id2 = courseService.addCourse("OPP")
    println(courseService.getCourseWithId(id1)) // OOP
    println(courseService.getCourseWithId(id2)) // OPP
    courseService.deleteCourseWithId(id1)
    //println(courseService.getCourseWithId(id1)) // CourseNotFoundException
}
