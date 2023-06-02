package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository

class UpdateBookNotesUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(bookNotes: BookNotesEntity) {
        return audiobooksRepository.updateBook(bookNotes)
    }

}