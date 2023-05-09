package cat.petrushkacat.audiobookplayer.data.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class AudiobooksRepositoryImpl(
    private val audiobooksDao: AudiobooksDao
): AudiobooksRepository {
    override suspend fun saveAfterParse(books: List<BookEntity>) {
        deleteBooksNotExist(books)
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


    private fun deleteBooksNotExist(books: List<BookEntity>) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            val listFromDB = audiobooksDao.getUris().map {
                it.folderUri
            }
            val listFromParse = books.map {
                it.folderUri
            }
            val listUrisToDelete = listFromDB.toSet().minus(listFromParse.toSet()).toList()
            audiobooksDao.deleteByUris(listUrisToDelete)
        }
    }

}