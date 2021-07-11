import java.lang.IllegalArgumentException
import java.time.LocalDate

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

    private val rentedBooks: MutableList<RentedBook> = mutableListOf()

    private data class RentedBook (
        val customerOIB: String,
        val book: Book,
        val dueDate: LocalDate
    )

    fun isBookAvailable(title: String, authorName: String): Boolean {
        for (book in booksInLibrary) {
            if (book.title == title && book.authorName == authorName && !isBookRented(book)) {
                return true
            }
        }
        return false
    }

    fun rentBook(title: String, authorName: String, customerOIB: String, duration: RentDuration): Book? {
        for (book in booksInLibrary) {
            if (book.title == title && book.authorName == authorName && !isBookRented(book)) {
                val dueDate = when (duration) {
                    RentDuration.TWO_WEEKS ->  LocalDate.now().plusWeeks(2)
                    RentDuration.MONTH -> LocalDate.now().plusMonths(1)
                    RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
                }
                rentedBooks.add(RentedBook(customerOIB, book, dueDate))
                return book
            }
        }
        return null
    }

    fun returnBook(book: Book) {
        for (i in rentedBooks.indices) {
            if (rentedBooks[i].book == book) {
                rentedBooks.removeAt(i)
                return
            }
        }
        throw IllegalArgumentException("This book is not rented.")
    }

    fun isBookRented(book: Book): Boolean {
        for (rentedBook in rentedBooks) {
            if (rentedBook.book == book) {
                return true
            }
        }
        return false
    }

    fun getRentedBooks(customerOIB: String): List<RentedBookInformation> {
        val rentedBooksInformation = mutableListOf<RentedBookInformation>()
        for (rentedBook in rentedBooks) {
            if (rentedBook.customerOIB == customerOIB) {
                rentedBooksInformation.add(RentedBookInformation(rentedBook.book, rentedBook.dueDate))
            }
        }
        return rentedBooksInformation
    }

}
