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

    private val rentedBooks: MutableMap<Book, BookRentInfo> = mutableMapOf()

    private data class BookRentInfo(
        val customerOIB: String,
        val dueDate: LocalDate
    )

    fun isBookAvailable(title: String, authorName: String) =
        booksInLibrary.any { book -> book.title == title && book.authorName == authorName && !isBookRented(book) }

    fun rentBook(title: String, authorName: String, customerOIB: String, duration: RentDuration): Book? {
        val bookToRent =
            booksInLibrary.find { book -> book.title == title && book.authorName == authorName && !isBookRented(book) }
        return bookToRent?.also {
            val dueDate = when (duration) {
                RentDuration.TWO_WEEKS -> LocalDate.now().plusWeeks(2)
                RentDuration.MONTH -> LocalDate.now().plusMonths(1)
                RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
            }
            rentedBooks[bookToRent] = BookRentInfo(customerOIB, dueDate)
        }
    }

    fun returnBook(book: Book) {
        rentedBooks.remove(book) ?: throw IllegalArgumentException("This book is not rented.")
    }

    fun isBookRented(book: Book) = book in rentedBooks

    fun getRentedBooks(customerOIB: String) = rentedBooks
        .filterValues { bookRentInfo -> bookRentInfo.customerOIB == customerOIB }
        .mapNotNull { (book, bookRentInfo) -> RentedBookInformation(book, bookRentInfo.dueDate) }

}
