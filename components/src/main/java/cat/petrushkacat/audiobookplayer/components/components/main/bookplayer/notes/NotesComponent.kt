package cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.notes

import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.Note
import kotlinx.coroutines.flow.StateFlow

interface NotesComponent {

    val models: StateFlow<BookNotesEntity>

    fun addNote(description: String)

    fun deleteNote(note: Note)

    fun editNote(note: Note, prevDescription: String)

    fun onNoteClick(chapterId: Int, time: Long)

    fun onBack()

}