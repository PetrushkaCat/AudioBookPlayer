package cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.notes

import android.content.Context
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.R
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.Note
import cat.petrushkacat.audiobookplayer.domain.models.Notes
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookNotesUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    private val getBookUseCase: GetBookUseCase,
    private val updateBookNotesUseCase: UpdateBookNotesUseCase,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val bookUri: String,
    private val onNoteClicked: (Int, Long) -> Unit,
    private val onBackClicked: () -> Unit
) : NotesComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()

    private var chapterNames = emptyList<String>()
    private val _models = MutableStateFlow(BookNotesEntity())
    override val models: StateFlow<BookNotesEntity> = _models.asStateFlow()

    init {
        val index = audiobookServiceHandler.currentTimings.value.currentChapterIndex
        val time = audiobookServiceHandler.currentTimings.value.currentTimeInChapter
        scope.launch {
            launch {
                getBookUseCase(bookUri).collect {
                    _models.value = BookNotesEntity(
                        folderName = it.folderName,
                        duration = it.duration,
                        notes = it.notes
                    )
                    chapterNames = it.chapters.chapters.map { chapter ->
                        chapter.name
                    }
                }
            }
            launch {
                delay(200)
                val saveNote = models.value.notes.notes.firstOrNull {
                    it.description == context.getString(R.string.automatic_note_description)
                }
                if(saveNote != null) {
                    if(index > saveNote.chapterIndex) {
                        deleteNote(saveNote)
                        delay(200)
                        addNote(context.getString(R.string.automatic_note_description))
                    } else if (index == saveNote.chapterIndex
                        && time > saveNote.time
                    ) {
                        deleteNote(saveNote)
                        delay(200)
                        addNote(context.getString(R.string.automatic_note_description))
                    }
                } else {
                    addNote(context.getString(R.string.automatic_note_description))
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
            updateBookNotesUseCase(BookNotesEntity(
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

            updateBookNotesUseCase(BookNotesEntity(
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

            updateBookNotesUseCase(BookNotesEntity(
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

    override fun onBack() {
        onBackClicked()
    }
}