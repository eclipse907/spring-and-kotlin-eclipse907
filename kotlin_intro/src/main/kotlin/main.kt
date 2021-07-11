fun main() {
    val customerOIB1 = "123456789"
    val customerOIB2 = "987654321"
    val customerOIB3 = "123123123"

    println(Library.isBookAvailable("Harry Potter", "J.K. Rowling")) // true
    println(Library.isBookAvailable("Harry Potter", "Harper Lee")) // false
    println(Library.isBookAvailable("Harry", "J.K. Rowling")) // false
    println(Library.isBookAvailable("The Lord of the Rings", "J.R.R. Tolkien")) // true
    println(Library.isBookAvailable("The Diary Of A Young Girl", "Anne Frank")) // true
    println("${Library.isBookAvailable("1984", "George Orwell")}\n") // true

    val book1 = Library.rentBook("The Lord of the Rings", "J.R.R. Tolkien", customerOIB1, RentDuration.TWO_WEEKS)
    println(Library.isBookRented(book1!!)) // true
    val book2 = Library.rentBook("1984", "George Orwell", customerOIB1, RentDuration.MONTH)
    println(Library.isBookRented(book2!!)) // true
    println(Library.isBookAvailable("1984", "George Orwell")) // false
    println(Library.rentBook("Harry", "J.K. Rowling", customerOIB1, RentDuration.TWO_WEEKS)) // null
    println("${Library.getRentedBooks(customerOIB1)}\n")

    val book3 = Library.rentBook("To Kill a Mockingbird", "Harper Lee", customerOIB2, RentDuration.TWO_MONTHS)
    println(Library.isBookRented(book3!!)) // true
    val book4 = Library.rentBook("The Great Gatsby", "F. Scott Fitzgerald", customerOIB2, RentDuration.MONTH)
    println(Library.isBookRented(book4!!)) // true
    println(Library.isBookAvailable("The Great Gatsby", "F. Scott Fitzgerald")) // false
    println(Library.rentBook("1984", "George Orwell", customerOIB2, RentDuration.TWO_WEEKS)) // null
    println("${Library.getRentedBooks(customerOIB2)}\n")

    println(Library.isBookAvailable("The Lord of the Rings", "J.R.R. Tolkien")) // true
    val book5 = Library.rentBook("The Lord of the Rings", "J.R.R. Tolkien", customerOIB3, RentDuration.MONTH)
    println(Library.isBookRented(book5!!)) // true
    println(Library.isBookAvailable("The Lord of the Rings", "J.R.R. Tolkien")) // true
    val book6 = Library.rentBook("The Lord of the Rings", "J.R.R. Tolkien", customerOIB3, RentDuration.TWO_WEEKS)
    println(Library.isBookRented(book6!!)) // true
    println(Library.isBookAvailable("The Lord of the Rings", "J.R.R. Tolkien")) // false
    println(Library.rentBook("The Lord of the Rings", "J.R.R. Tolkien", customerOIB3, RentDuration.TWO_WEEKS)) // null
    println("${Library.getRentedBooks(customerOIB3)}\n")

    Library.returnBook(book1)
    println(Library.isBookRented(book1)) // false
    println(Library.isBookAvailable("The Lord of the Rings", "J.R.R. Tolkien")) // true
    println("${Library.getRentedBooks(customerOIB1)}\n")

    Library.returnBook(book4)
    println(Library.isBookRented(book4)) // false
    println(Library.isBookAvailable("The Great Gatsby", "F. Scott Fitzgerald")) // true
    println(Library.getRentedBooks(customerOIB2))

    //Library.returnBook(book4) // throws IllegalArgumentException
}
