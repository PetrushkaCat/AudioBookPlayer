package cat.petrushkacat.audiobookplayer.core.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import kotlinx.coroutines.flow.Flow

interface AudiobooksRepository {

    suspend fun saveAfterParse(books: List<BookEntity>)

    suspend fun saveBookAfterParse(book: BookEntity)

    suspend fun getAllBooks(): Flow<List<BooksListComponent.Model>>

    suspend fun getBooksInFolder(rootFolder: Uri): Flow<List<BooksListComponent.Model>>


    suspend fun updateBook(book: BookEntity)

    suspend fun getBook(bookFolder: Uri): Flow<BookEntity>
}