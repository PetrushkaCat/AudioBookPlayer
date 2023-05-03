package cat.petrushkacat.audiobookplayer.data.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDao
import cat.petrushkacat.audiobookplayer.data.model.BookEntity

class AudiobooksRepositoryImpl(
    private val audiobooksDao: AudiobooksDao
): AudiobooksRepository {
    override fun saveAfterParse(books: List<BookComponent.Model>) {
        TODO("Not yet implemented")
    }

    override fun saveBook(book: BookComponent.Model) {
        TODO("Not yet implemented")
    }

    override fun getAllBooks(): List<BooksListComponent.Model> {
        TODO("Not yet implemented")
    }

    override fun getBooksInFolder(rootFolder: Uri): List<BooksListComponent.Model> {
        TODO("Not yet implemented")
    }

    override fun getBook(bookFolder: Uri): BookComponent.Model {
        TODO("Not yet implemented")
    }


    companion object {
        private fun mapToDB(book: BookComponent.Model): BookEntity {
            return BookEntity(
                folderUri = book.folderUri.toString(),
                name = book.name,
                currentTime = book.currentTime,
                duration = book.duration,
                rootFolderUri = book.rootFolderUri.toString(),
                imageUri = book.imageUri.toString()
            )
        }

        private fun mapToCore(book: BookEntity): BookComponent.Model {
            return BookComponent.Model(
                folderUri = Uri.parse(book.folderUri),
                name = book.name,
                currentTime = book.currentTime,
                duration = book.duration,
                rootFolderUri = Uri.parse(book.rootFolderUri),
                imageUri = Uri.parse(book.imageUri),
                chapters = emptyList()
            )
        }
    }
}