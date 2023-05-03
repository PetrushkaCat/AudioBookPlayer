package cat.petrushkacat.audiobookplayer.core.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent

interface AudiobooksRepository {

    fun saveAfterParse(books: List<BookComponent.Model>)

    fun saveBook(book: BookComponent.Model)

    fun getAllBooks(): List<BooksListComponent.Model>

    fun getBooksInFolder(rootFolder: Uri): List<BooksListComponent.Model>

    fun getBook(bookFolder: Uri): BookComponent.Model
}