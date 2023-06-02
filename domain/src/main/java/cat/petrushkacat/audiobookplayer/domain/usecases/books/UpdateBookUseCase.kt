package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository

class UpdateBookUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(book: BookEntity) {
        return audiobooksRepository.updateBook(book)
    }

}