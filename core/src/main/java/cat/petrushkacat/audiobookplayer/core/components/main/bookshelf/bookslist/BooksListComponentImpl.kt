package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.core.components.main.folderselector.extractInt
import cat.petrushkacat.audiobookplayer.core.components.main.folderselector.isAudio
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.Chapter
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeIO
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BooksListComponentImpl(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository,
    private val audiobooksRepository: AudiobooksRepository,
    private val rootFoldersRepository: RootFoldersRepository,
    private val context: Context,
    val onBookSelected: (Uri) -> Unit,
    books: StateFlow<List<BooksListComponent.Model>>
) : BooksListComponent, ComponentContext by componentContext {


    private val scopeIO = componentCoroutineScopeIO()
    private val scope = componentCoroutineScopeDefault()

    private val _models = MutableStateFlow<List<BooksListComponent.Model>>(emptyList())
    override val models: StateFlow<List<BooksListComponent.Model>> = _models

    private val _settings = MutableStateFlow(SettingsEntity())
    override val settings: StateFlow<SettingsEntity> = _settings.asStateFlow()

    override val foldersToProcess = _foldersToProcess.asStateFlow()
    override val foldersProcessed = _foldersProcessed.asStateFlow()
    override val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var folderUris: List<Uri> = emptyList()

    init {
        scope.launch {
            val started: MutableList<BooksListComponent.Model> = mutableListOf()
            val completed: MutableList<BooksListComponent.Model> = mutableListOf()
            val notStarted: MutableList<BooksListComponent.Model> = mutableListOf()

            launch {
                settingsRepository.getSettings().collect {
                    _settings.value = it
                }
            }
            launch {
                books.collect { list ->
                    list.forEach {
                        if (it.isStarted && !it.isCompleted) {
                            started.add(it)
                        } else if (it.isCompleted) {
                            completed.add(it)
                        } else {
                            notStarted.add(it)
                        }
                    }
                    started.sortByDescending {
                        it.lastTimeListened
                    }

                    started += notStarted + completed
                    _models.value = started.toMutableList()

                    started.clear()
                    completed.clear()
                    notStarted.clear()
                }
            }
            launch {
                rootFoldersRepository.getFolders().collect { list ->
                    folderUris = list.map { Uri.parse(it.uri) }
                }
            }
            launch {
                _foldersProcessed.collect {
                    if(it == _foldersToProcess.value && it > 0) {
                        Log.d("refreshing", bookUris.toString())
                        audiobooksRepository.deleteIfNoInList(bookUris)
                        _isRefreshing.value = false
                        _foldersProcessed.value = 0
                        _foldersToProcess.value = 0
                        bookUris.clear()
                    }
                }
            }
        }
    }
    override fun onBookClick(uri: Uri) {
        onBookSelected(uri)
    }

    override fun refresh() {
        _isRefreshing.value = true
        _foldersProcessed.value = 0
        _foldersToProcess.value = 0
        Log.d("refreshing", _isRefreshing.value.toString())
        Log.d("refreshing", _foldersProcessed.value.toString())
        Log.d("refreshing", _foldersToProcess.value.toString())
        for(uri in folderUris) {
            scope.launch {
                parseBooks(uri)
            }
        }
    }

    private fun parseBooks(folderUri: Uri) {
        scopeIO.launch {
            val file = DocumentFile.fromTreeUri(context, folderUri)!!
            parseCycle(file, folderUri)
        }
    }

    private fun parseCycle(bookFolder: DocumentFile, rootFolderUri: Uri) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
        _foldersToProcess.value += 1
            var name: String? = null
            var image: ByteArray? = null
            var bookDuration: Long = 0

            val chapters: MutableList<Chapter> = mutableListOf()

            val mmr = MediaMetadataRetriever()

            bookFolder.listFiles().forEachIndexed { index, content ->
                if (content == null) return@forEachIndexed
                if (content.isDirectory) {
                    parseCycle(content, rootFolderUri)
                }
                if (content.isAudio()) {
                    mmr.setDataSource(context, content.uri)
                    name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: bookFolder.name!!

                    val chapterDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()!!
                    bookDuration += chapterDuration

                    if(image == null) {
                        image = mmr.embeddedPicture
                    }

                    chapters.add(
                        Chapter(bookFolder.uri.toString(),
                        content.name?.substringBeforeLast('.') ?: "Chapter ${index + 1}",
                        chapterDuration,
                        0,
                        content.uri.toString())
                    )
                }

            }

            name?.let {

                val sortedChapters = chapters.sortedWith { a, b ->
                    extractInt(a) - extractInt(b)
                }

                var timeFromBeginning = 0L
                sortedChapters.forEach {
                    it.timeFromBeginning = timeFromBeginning
                    timeFromBeginning += it.duration
                }

                if(!isActive) return@launch
                audiobooksRepository.saveBookAfterParse(
                    BookEntity(
                        folderUri = bookFolder.uri.toString(),
                        folderName = bookFolder.name!!,
                        name = name!!,
                        chapters = Chapters(sortedChapters),
                        currentChapter = 0,
                        currentChapterTime = 0,
                        currentTime = 0,
                        duration = bookDuration,
                        rootFolderUri = rootFolderUri.toString(),
                        image = image,
                    )
                )
                bookUris.add(bookFolder.uri)
                Log.d("refreshing", "saved")
            }
            _foldersProcessed.value += 1
        }
    }

    companion object {
        private val _foldersToProcess = MutableStateFlow(0)
        private val _foldersProcessed = MutableStateFlow(0)
        private val _isRefreshing = MutableStateFlow(false)
        private val bookUris: MutableList<Uri> = mutableListOf()

    }
}