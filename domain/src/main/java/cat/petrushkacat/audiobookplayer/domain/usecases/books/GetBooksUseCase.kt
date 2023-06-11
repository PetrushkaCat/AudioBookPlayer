package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetBooksUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {
    suspend operator fun invoke(booksType: BooksType): Flow<List<BookListEntity>> {
        return when (booksType) {
            BooksType.All -> {
                audiobooksRepository.getAllBookListEntities().map { books ->
                    sortBooks(books)
                }
            }

            is BooksType.Folder -> {
                audiobooksRepository.getAllBookListEntitiesInFolder(booksType.folderUri).map { books ->
                    sortBooks(books)
                }
            }

            BooksType.Completed -> {
                audiobooksRepository.getAllBookListEntities().map {
                    it.filter { book ->
                        book.isCompleted
                    }
                }
            }

            BooksType.Favorites -> {
                audiobooksRepository.getAllBookListEntities().map {
                    it.filter { book ->
                        book.isFavorite
                    }
                }
            }

            BooksType.ListenLater -> {
                audiobooksRepository.getAllBookListEntities().map {
                    it.filter { book ->
                        book.isWantToListen
                    }
                }
            }
        }
    }

    private suspend fun sortBooks(books: List<BookListEntity>): List<BookListEntity> = withContext(Dispatchers.Default) {
        val started: MutableList<BookListEntity> = mutableListOf()
        val completed: MutableList<BookListEntity> = mutableListOf()
        val notStarted: MutableList<BookListEntity> = mutableListOf()
        books.forEach { book ->
                if (book.isStarted && !book.isCompleted) {
                    started.add(book)
                } else if (book.isCompleted) {
                    completed.add(book)
                } else {
                    notStarted.add(book)
                }
            }
            started.sortByDescending { startedBook ->
                startedBook.lastTimeListened
            }

            started += notStarted + completed
            started.toMutableList()
    }

    sealed interface BooksType {

        object All : BooksType

        data class Folder(val folderUri: String) : BooksType

        object Favorites : BooksType

        object ListenLater : BooksType

        object Completed : BooksType
    }
}

