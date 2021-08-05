package courseservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Conditional
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption

@Component
@Conditional(CourseRepositoryInFileTypeCondition::class)
class InFileCourseRepository(
    private val coursesFile: Resource
) : CourseRepository {

    init {
        if (!coursesFile.exists()) {
            coursesFile.file.createNewFile()
        }
    }

    override fun insert(name: String): Long {
        val newId = (coursesFile.file.readLines().filter { line -> line.isNotBlank() }
            .map { line -> line.trim().split(",")[0].toLong() }
            .maxOrNull() ?: 0) + 1
        return newId.also { coursesFile.file.appendText("$newId,$name\n") }
    }

    override fun findById(id: Long) = coursesFile.file.readLines().filter { line -> line.isNotBlank() }
        .find { line -> line.trim().startsWith("$id") }
        ?.let {
            val tokens = it.trim().split(",")
            Course(tokens[0].toLong(), tokens[1])
        } ?: throw CourseNotFoundException(id)

    override fun deleteById(id: Long): Course {
        val lines = coursesFile.file.readLines()
        var lineToDelete: String? = null
        FileOutputStream(coursesFile.file).bufferedWriter()
            .use {
                lines.filter { line -> line.isNotBlank() }
                    .forEach { line ->
                        if (line.trim().startsWith("$id")) {
                            lineToDelete = line
                        } else {
                            it.appendLine(line.trim())
                        }
                    }
            }
        return lineToDelete?.let {
            val tokens = it.trim().split(",")
            Course(tokens[0].toLong(), tokens[1])
        } ?: throw CourseNotFoundException(id)
    }

}
