import java.time.LocalDate

data class RentedBookInformation(
    val book: Book,
    val dueDate: LocalDate
) {
    override fun toString() = "($book) dueDate=$dueDate"
}
