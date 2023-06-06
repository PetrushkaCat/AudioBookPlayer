package cat.petrushkacat.audiobookplayer.domain.usecases.books

import android.util.Log
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.Note
import cat.petrushkacat.audiobookplayer.domain.models.Notes
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.first

class UpdateNoteUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    /**
     * Chapter name will be updated automatically
     */
    suspend operator fun invoke(
        note: Note,
        bookUri: String,
        newDescription: String,
        newChapterIndex: Int,
        newTime: Long,
    ) {

        val book = audiobooksRepository.getBook(bookUri).first()
        val notes = book.notes.notes.toMutableList()
        var noteIndex = -1

        notes.forEachIndexed { index, it ->
            if (note.description == it.description
                && note.chapterIndex == it.chapterIndex
                && note.time == it.time
            ) {
                noteIndex = index
                return@forEachIndexed
            }
        }
        Log.d("note index ", noteIndex.toString())
        try {
            notes.removeAt(noteIndex)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        notes.add(
            note.copy(
                chapterName = book.chapters.chapters[newChapterIndex].name,
                description = newDescription,
                chapterIndex = newChapterIndex,
                time = newTime,
            )
        )

        audiobooksRepository.updateBook(
            BookNotesEntity(
                folderName = book.folderName,
                duration = book.duration,
                notes = Notes(notes)
            )
        )

    }

}