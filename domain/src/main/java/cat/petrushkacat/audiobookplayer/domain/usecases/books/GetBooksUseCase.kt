package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GetBooksUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {
    suspend operator fun invoke(booksType: BooksType): Flow<List<BookListEntity>> {
        return when(booksType) {
            BooksType.All -> {
                audiobooksRepository.getAllBookListEntities()
            }
            BooksType.Completed -> {
                audiobooksRepository.getAllBookListEntities().onEach {
                    it.filter {book ->
                        book.isCompleted
                    }
                }
            }
            BooksType.Favorites -> {
                audiobooksRepository.getAllBookListEntities().onEach {
                    it.filter {book ->
                        book.isFavorite
                    }
                }
            }
            is BooksType.Folder -> {
                audiobooksRepository.getAllBookListEntitiesInFolder(booksType.folderUri)
            }
            BooksType.ListenLater -> {
                audiobooksRepository.getAllBookListEntities().onEach {
                    it.filter {book ->
                        book.isWantToListen
                    }
                }
            }
        }
    }

    sealed interface BooksType {

        object All: BooksType

        data class Folder(val folderUri: String): BooksType

        object Favorites: BooksType

        object ListenLater: BooksType

        object Completed: BooksType
    }
}

