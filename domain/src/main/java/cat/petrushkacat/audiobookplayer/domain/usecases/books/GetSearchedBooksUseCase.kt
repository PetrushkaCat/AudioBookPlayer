package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetSearchedBooksUseCase(
    private val getBooksUseCase: GetBooksUseCase
) {
    suspend operator fun invoke(text: String, folderUri: String?): Flow<List<BookListEntity>> {
        if(text.trim().isEmpty()) {
            return if(folderUri != null) {
                getBooksUseCase(GetBooksUseCase.BooksType.Folder(folderUri))
            } else {
                getBooksUseCase(GetBooksUseCase.BooksType.All)
            }
        }

        val books = getBooksUseCase(GetBooksUseCase.BooksType.All)
            .first()
            .filter {
                it.name.contains(text, true)
            }

        return flow {
            emit(books)
        }
    }
}