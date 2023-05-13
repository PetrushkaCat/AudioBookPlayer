package cat.petrushkacat.audiobookplayer.data.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDao


class AudiobooksRepositoryImpl(
    private val audiobooksDao: AudiobooksDao
): AudiobooksRepository {
    override suspend fun saveAfterParse(books: List<BookEntity>) {
        audiobooksDao.saveAfterParse(books)
    }

    override suspend fun saveBookAfterParse(book: BookEntity) {
        audiobooksDao.saveBookAfterParse(book)
    }

    override suspend fun getAllBooks() = audiobooksDao.getAll()

    override suspend fun getBooksInFolder(rootFolder: Uri) = audiobooksDao.getBooksInFolder(rootFolder.toString())

    override suspend fun updateBook(book: BookPlayerComponent.UpdateInfo) {
        audiobooksDao.updateBook(book)
    }

    override suspend fun updateBook(book: NotesComponent.Model) {
        audiobooksDao.updateBook(book)
    }

    override suspend fun getBook(bookFolder: Uri) = audiobooksDao.getBook(bookFolder.toString())
    override suspend fun deleteAllInFolder(rootFolderUri: String) {
        audiobooksDao.deleteAllInFolder(rootFolderUri)
    }

    override suspend fun deleteBook(uri: Uri) {
        audiobooksDao.deleteBook(uri.toString())
    }

    override suspend fun deleteIfNoInList(uris: List<Uri>) {
        audiobooksDao.deleteIfNoInList(uris.map { it.toString() })
    }

}