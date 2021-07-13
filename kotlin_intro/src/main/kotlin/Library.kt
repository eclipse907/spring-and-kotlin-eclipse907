import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.util.function.Predicate

object Library {

    private val booksInLibrary: List<Book> = listOf(
        Book(1, "Harry Potter", "J.K. Rowling"),
        Book(2, "Harry Potter", "J.K. Rowling"),
        Book(3, "To Kill a Mockingbird", "Harper Lee"),
        Book(4, "1984", "George Orwell"),
        Book(5, "The Lord of the Rings", "J.R.R. Tolkien"),
        Book(6, "The Lord of the Rings", "J.R.R. Tolkien"),
        Book(7, "The Great Gatsby", "F. Scott Fitzgerald"),
        Book(8, "The Diary Of A Young Girl", "Anne Frank"),
        Book(9, "The Hobbit", "J.R.R. Tolkien"),
        Book(10, "The Lord of the Rings", "J.R.R. Tolkien")
    )

    private val rentedBooks: MutableMap<String, MutableList<RentedBook>> = mutableMapOf()

    private data class RentedBook (
        val customerOIB: String,
        val book: Book,
        val dueDate: LocalDate
    )

    fun isBookAvailable(title: String, authorName: String) =
        booksInLibrary.any { book ->  book.title == title && book.authorName == authorName && !isBookRented(book)}

    fun rentBook(title: String, authorName: String, customerOIB: String, duration: RentDuration): Book? {
        val bookToRent =
            booksInLibrary.find { book -> book.title == title && book.authorName == authorName && !isBookRented(book)}
        if (bookToRent != null) {
            val dueDate = when (duration) {
                RentDuration.TWO_WEEKS ->  LocalDate.now().plusWeeks(2)
                RentDuration.MONTH -> LocalDate.now().plusMonths(1)
                RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
            }
            val customerRentedBooks = rentedBooks.getOrElse(customerOIB) {
                val rentedBooksList = mutableListOf<RentedBook>()
                rentedBooks[customerOIB] = rentedBooksList
                rentedBooksList
            }
            customerRentedBooks.add(RentedBook(customerOIB, bookToRent, dueDate))
        }
        return bookToRent
    }

    fun returnBook(book: Book) {
        for ((_, value) in rentedBooks) {
            if (value.removeIf { rentedBook -> rentedBook.book == book }) {
                return
            }
        }
        throw IllegalArgumentException("This book is not rented.")
    }

    fun isBookRented(book: Book): Boolean {
        for ((_, value) in rentedBooks) {
            if (value.any { rentedBook -> rentedBook.book == book }) {
                return true
            }
        }
        return false
    }

    fun getRentedBooks(customerOIB: String): List<RentedBookInformation> {
        val rentedBooksInformation = mutableListOf<RentedBookInformation>()
        rentedBooks[customerOIB]?.forEach { rentedBook ->
            rentedBooksInformation.add(RentedBookInformation(rentedBook.book, rentedBook.dueDate))
        }
        return rentedBooksInformation
    }

}
