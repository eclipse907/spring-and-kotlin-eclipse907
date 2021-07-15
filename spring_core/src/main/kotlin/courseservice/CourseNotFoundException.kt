package courseservice

class CourseNotFoundException(id: Long) : RuntimeException("Course with and ID $id not found")
