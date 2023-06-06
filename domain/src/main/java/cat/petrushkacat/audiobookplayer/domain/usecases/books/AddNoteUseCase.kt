package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.Note
import cat.petrushkacat.audiobookplayer.domain.models.Notes
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.first

class AddNoteUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(note: Note, bookUri: String) {
        val book = audiobooksRepository.getBook(bookUri).first()
        val notes = book.notes.notes.toMutableList()
        notes.add(note)

        notes.sortWith(
            compareBy(
                { it.chapterIndex },
                { it.time }))

        audiobooksRepository.updateBook(BookNotesEntity(
            folderName = book.folderName,
            duration = book.duration,
            notes = Notes(notes)
        ))
    }
}