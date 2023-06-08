package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.Flow

class GetBooksUrisUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(): Flow<List<BookUri>> {
        return audiobooksRepository.getBooksUris()
    }

}