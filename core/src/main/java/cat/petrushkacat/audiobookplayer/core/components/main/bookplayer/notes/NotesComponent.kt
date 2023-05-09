package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes

import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.models.Note
import cat.petrushkacat.audiobookplayer.core.models.Notes
import kotlinx.coroutines.flow.StateFlow

interface NotesComponent {

    val models: StateFlow<Model>

    fun addNote(description: String)

    fun deleteNote(note: Note)

    fun editNote(note: Note, prevDescription: String)

    fun onNoteClick(chapterId: Int, time: Long)

    data class Model(
        val folderName: String = "",
        val duration: Long = 0L,
        val currentChapter: Int = 0,
        val currentChapterTime: Long = 0L,
        val notes: Notes = Notes(emptyList())
    )

}