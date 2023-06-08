package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.data.db.dao.AudiobooksDao
import cat.petrushkacat.audiobookplayer.data.mappers.toBookEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.Flow


class AudiobooksRepositoryImpl(
    private val audiobooksDao: AudiobooksDao
): AudiobooksRepository {
    override suspend fun saveAfterParse(books: List<BookEntity>) {
        audiobooksDao.saveAfterParse(books.map { it.toBookEntityDB() })
    }

    override suspend fun saveBookAfterParse(book: BookEntity) {
        audiobooksDao.saveBookAfterParse(book.toBookEntityDB())
    }

    override suspend fun getAllBookListEntities() = audiobooksDao.getAllBookListEntities()

    override suspend fun getAllBookListEntitiesInFolder(rootFolder: String) = audiobooksDao.getAllBookListEntitiesInFolder(rootFolder)

    override suspend fun updateBook(book: BookNotesEntity) {
        audiobooksDao.updateBook(book)
    }

    override suspend fun updateBook(book: BookEntity) {
        audiobooksDao.updateBook(book.toBookEntityDB())
    }

    override suspend fun getBook(bookFolder: String) = audiobooksDao.getBook(bookFolder)
    override suspend fun deleteAllInFolder(rootFolderUri: String) {
        audiobooksDao.deleteAllInFolder(rootFolderUri)
    }

    override suspend fun deleteBook(uri: String) {
        audiobooksDao.deleteBook(uri)
    }

    override suspend fun deleteIfNoInList(uris: List<String>, rootFolderUris: List<String>) {
        audiobooksDao.deleteIfNoInList(uris, rootFolderUris)
    }

    override suspend fun getBooksUris(): Flow<List<BookUri>> {
        return audiobooksDao.getUris()
    }

}