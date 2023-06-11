package cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.notes

import android.content.Context
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.Note
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.books.AddNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    private val getBookUseCase: GetBookUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val bookUri: String,
    private val onNoteClicked: (Int, Long) -> Unit,
    private val onBackClicked: () -> Unit
) : NotesComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()

    private var chapterNames = emptyList<String>()
    private val _models = MutableStateFlow(BookNotesEntity())
    override val models: StateFlow<BookNotesEntity> = _models.asStateFlow()

    private val _settings = MutableStateFlow(SettingsEntity())
    override val settings: StateFlow<SettingsEntity> = _settings.asStateFlow()

    init {
        val index = audiobookServiceHandler.currentTimings.value.currentChapterIndex
        val time = audiobookServiceHandler.currentTimings.value.currentTimeInChapter
        scopeIO.launch {
            launch {
                getSettingsUseCase().collect {
                    _settings.value = it
                }
            }
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
                delay(100)
                val saveNote = models.value.notes.notes.firstOrNull {
                    it.description == context.getString(cat.petrushkacat.audiobookplayer.strings.R.string.automatic_max_time_note_description)
                }
                if(saveNote != null) {
                    if(index > saveNote.chapterIndex) {
                        updateNoteUseCase(
                            saveNote,
                            bookUri,
                            context.getString(cat.petrushkacat.audiobookplayer.strings.R.string.automatic_max_time_note_description),
                            index,
                            time
                        )
                    } else if (index == saveNote.chapterIndex && time > saveNote.time) {
                        updateNoteUseCase(
                            saveNote,
                            bookUri,
                            context.getString(cat.petrushkacat.audiobookplayer.strings.R.string.automatic_max_time_note_description),
                            index,
                            time
                        )
                    }
                } else {
                    addNote(context.getString(cat.petrushkacat.audiobookplayer.strings.R.string.automatic_max_time_note_description))
                }
            }
        }
    }

    override fun addNote(description: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val index = audiobookServiceHandler.currentTimings.value.currentChapterIndex
            val time = audiobookServiceHandler.currentTimings.value.currentTimeInChapter
            addNoteUseCase(
                Note(
                    chapterIndex = index,
                    chapterName = chapterNames[index],
                    time = time,
                    description = description
                ),
                bookUri
            )
        }
    }

    override fun deleteNote(note: Note) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            deleteNoteUseCase(note, bookUri)
        }
    }

    override fun editNote(note: Note, newDescription: String) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            updateNoteUseCase(note, bookUri, newDescription, note.chapterIndex, note.time)
        }
    }

    override fun onNoteClick(chapterId: Int, time: Long) {
       onNoteClicked(chapterId, time)
    }

    override fun onBack() {
        onBackClicked()
    }
}