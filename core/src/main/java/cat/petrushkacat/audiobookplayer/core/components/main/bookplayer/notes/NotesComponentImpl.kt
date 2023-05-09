package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes

import android.net.Uri
import android.provider.ContactsContract
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookComponent
import cat.petrushkacat.audiobookplayer.core.models.Note
import cat.petrushkacat.audiobookplayer.core.models.Notes
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotesComponentImpl(
    componentContext: ComponentContext,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val bookUri: Uri,
    private val onNoteClicked: (Int, Long) -> Unit
) : NotesComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()

    private var chapterNames = emptyList<String>()
    private val _models = MutableStateFlow(NotesComponent.Model())
    override val models: StateFlow<NotesComponent.Model> = _models.asStateFlow()

    init {
        scope.launch {
            audiobooksRepository.getBook(bookUri).collect {
                _models.value = NotesComponent.Model(
                    folderName = it.folderName,
                    duration = it.duration,
                    notes = it.notes
                )
                chapterNames = it.chapters.chapters.map { chapter ->
                    chapter.name
                }
            }
        }
    }

    override fun addNote(description: String) {
        val notes = models.value.notes.notes.toMutableList()

        val index = audiobookServiceHandler.currentTimings.value.currentChapterIndex
        val time = audiobookServiceHandler.currentTimings.value.currentTimeInChapter
        scope.launch {
            notes.add(
                Note(
                    chapterIndex = index,
                    chapterName = chapterNames[index],
                    time = time,
                    description = description
                )
            )
            notes.sortWith(
                compareBy(
                    { it.chapterIndex },
                    { it.time }))
            audiobooksRepository.updateBook(NotesComponent.Model(
                folderName = models.value.folderName,
                duration = models.value.duration,
                currentChapter = index,
                currentChapterTime = time,
                notes = Notes(notes)
            ))
        }
    }

    override fun deleteNote(note: Note) {
        val notes = models.value.notes.notes.toMutableList()

        val index = audiobookServiceHandler.currentTimings.value.currentChapterIndex
        val time = audiobookServiceHandler.currentTimings.value.currentTimeInChapter
        scope.launch {
            notes.remove(note)

            audiobooksRepository.updateBook(NotesComponent.Model(
                folderName = models.value.folderName,
                duration = models.value.duration,
                currentChapter = index,
                currentChapterTime = time,
                notes = Notes(notes)
            ))
        }
    }

    override fun editNote(note: Note, prevDescription: String) {
        val notes = models.value.notes.notes.toMutableList()

        val index = audiobookServiceHandler.currentTimings.value.currentChapterIndex
        val time = audiobookServiceHandler.currentTimings.value.currentTimeInChapter

        var noteIndex = 0
        scope.launch {
            notes.forEachIndexed { index, it ->
                if( it.time == note.time && it.chapterIndex == note.chapterIndex && it.description == prevDescription) {
                    noteIndex = index
                    return@forEachIndexed
                }
            }
            notes.removeAt(noteIndex)
            notes.add(note)

            audiobooksRepository.updateBook(NotesComponent.Model(
                folderName = models.value.folderName,
                duration = models.value.duration,
                currentChapter = index,
                currentChapterTime = time,
                notes = Notes(notes)
            ))
        }
    }

    override fun onNoteClick(chapterId: Int, time: Long) {
       onNoteClicked(chapterId, time)
    }
}