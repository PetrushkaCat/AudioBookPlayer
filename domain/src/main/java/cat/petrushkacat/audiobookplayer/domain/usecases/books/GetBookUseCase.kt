package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.Flow

class GetBookUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(bookUri: String): Flow<BookEntity> {
        return audiobooksRepository.getBook(bookUri)
    }
}