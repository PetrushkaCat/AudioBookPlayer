package cat.petrushkacat.audiobookplayer.core.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import kotlinx.coroutines.flow.Flow

interface AudiobooksRepository {

    suspend fun saveAfterParse(books: List<BookEntity>)

    suspend fun saveBookAfterParse(book: BookEntity)

    suspend fun getAllBooks(): Flow<List<BooksListComponent.Model>>

    suspend fun getBooksInFolder(rootFolder: Uri): Flow<List<BooksListComponent.Model>>

    suspend fun updateBook(book: BookPlayerComponent.UpdateInfo)

    suspend fun updateBook(book: NotesComponent.Model)

    suspend fun getBook(bookFolder: Uri): Flow<BookEntity>

    suspend fun deleteAllInFolder(rootFolderUri: String)

    suspend fun deleteBook(uri: Uri)

    suspend fun deleteIfNoInList(uris: List<Uri>)

}