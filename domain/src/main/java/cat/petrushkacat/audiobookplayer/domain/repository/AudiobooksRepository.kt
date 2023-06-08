package cat.petrushkacat.audiobookplayer.domain.repository

import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import kotlinx.coroutines.flow.Flow

interface AudiobooksRepository {

    suspend fun saveAfterParse(books: List<BookEntity>)

    suspend fun saveBookAfterParse(book: BookEntity)

    suspend fun getAllBookListEntities(): Flow<List<BookListEntity>>

    suspend fun getAllBookListEntitiesInFolder(rootFolder: String): Flow<List<BookListEntity>>

    suspend fun updateBook(book: BookNotesEntity)

    suspend fun getBook(bookFolder: String): Flow<BookEntity>

    suspend fun deleteAllInFolder(rootFolderUri: String)

    suspend fun deleteBook(uri: String)

    suspend fun deleteIfNoInList(uris: List<String>, rootFolderUris: List<String>)

    suspend fun updateBook(book: BookEntity)

    suspend fun getBooksUris(): Flow<List<BookUri>>
}