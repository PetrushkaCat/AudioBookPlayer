package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class GetSearchedBooksUseCase(
    private val getBooksUseCase: GetBooksUseCase
) {
    suspend operator fun invoke(text: String): Flow<List<BookListEntity>> {
        if(text.isEmpty()) return getBooksUseCase(GetBooksUseCase.BooksType.All)

        return getBooksUseCase(GetBooksUseCase.BooksType.All).onEach {
            it.filter { book ->
                book.name.contains(text)
            }
        }
    }
}