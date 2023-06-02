package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository

class DeleteBookUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(bookUri: String) {
        return audiobooksRepository.deleteBook(bookUri)
    }

}