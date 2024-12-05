data class Book(
    val title: String,
    val author: String,
    val isbn: String,
    var available: Boolean = true,
    var borrower: Member? = null
) {
    fun isAvailable(): Boolean = available

    fun borrow(member: Member) {
        if (available) {
            available = false
            borrower = member
            member.borrow_book(this)
        } else {
            println("Book is not available.")
        }
    }

    fun returnBook() {
        available = true
        borrower?.return_book(this)
        borrower = null
    }
}
class Member(
    val name: String,
    val memberId: String,
    val maxBooks: Int = 3
) {
    val borrowedBooks: MutableList<Book> = mutableListOf()

    fun borrow_book(book: Book) {
        if (can_borrow()) {
            borrowedBooks.add(book)
        } else {
            println("$name can't borrow more books.")
        }
    }

    fun return_book(book: Book) {
        borrowedBooks.remove(book)
    }

    fun can_borrow(): Boolean = borrowedBooks.size < maxBooks
}
class Library {
    val books: MutableList<Book> = mutableListOf()
    val members: MutableList<Member> = mutableListOf()

    fun add_book(book: Book) {
        books.add(book)
    }

    fun remove_book(book: Book) {
        books.remove(book)
    }

    fun find_book_by_title(title: String): Book? {
        return books.find { it.title == title }
    }

    fun register_member(member: Member) {
        members.add(member)
    }

    fun unregister_member(member: Member) {
        members.remove(member)
    }

    fun borrow_book(member: Member, book: Book) {
        if (book.isAvailable() && member.can_borrow()) {
            book.borrow(member)
        } else {
            println("Cannot borrow book.")
        }
    }

    fun return_book(member: Member, book: Book) {
        book.returnBook()
    }

    fun get_books_count(): Int = books.size

    fun get_available_books_count(): Int = books.count { it.isAvailable() }

    fun get_books_count_by_title(title: String): Int {
        return books.count { it.title == title }
    }

    fun get_books_count_by_author(author: String): Int {
        return books.count { it.author == author }
    }
}
class Librarian(
    val name: String,
    val employeeId: String
) {
    fun add_book_to_library(book: Book, library: Library) {
        library.add_book(book)
    }

    fun remove_book_from_library(book: Book, library: Library) {
        library.remove_book(book)
    }

    fun register_new_member(member: Member, library: Library) {
        library.register_member(member)
    }
}
fun main() {
    val library = Library()
    val librarian = Librarian("John Doe", "L123")
    val member = Member("Alice", "M001")

    librarian.register_new_member(member, library)

    val book1 = Book("Kotlin Programming", "JetBrains", "123-456-789")
    val book2 = Book("Advanced Kotlin", "JetBrains", "987-654-321")

    librarian.add_book_to_library(book1, library)
    librarian.add_book_to_library(book2, library)

    println("Total books in library: ${library.get_books_count()}")
    println("Available books: ${library.get_available_books_count()}")

    librarian.add_book_to_library(book1, library)

    println("Total books by JetBrains: ${library.get_books_count_by_author("JetBrains")}")

    // Alice borrows a book
    library.borrow_book(member, book1)

    println("Books borrowed by Alice: ${member.borrowedBooks.size}")
    println("Is 'Kotlin Programming' available? ${book1.isAvailable()}")

    // Alice returns the book
    library.return_book(member, book1)
    println("Books borrowed by Alice: ${member.borrowedBooks.size}")
    println("Is 'Kotlin Programming' available? ${book1.isAvailable()}")
}