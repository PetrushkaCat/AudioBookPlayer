package cat.petrushkacat.audiobookplayer.components.components.main.folderselector

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.components.states.RefreshingStates
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.components.util.extractInt
import cat.petrushkacat.audiobookplayer.components.util.isAudio
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.Chapter
import cat.petrushkacat.audiobookplayer.domain.models.Chapters
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteAllBooksInFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.SaveBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.AddFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.DeleteFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Date

class FoldersComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val addFolderUseCase: AddFolderUseCase,
    private val deleteAllBooksInFolderUseCase: DeleteAllBooksInFolderUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    val onBackClicked: () -> Unit
) : FoldersComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()

    private val _models: MutableStateFlow<List<RootFolderEntity>> = MutableStateFlow(emptyList())
    override val models: StateFlow<List<RootFolderEntity>> = _models.asStateFlow()

    override val foldersToProcess = _foldersToProcess.asStateFlow()
    override val foldersProcessed = _foldersProcessed.asStateFlow()

    init {
        scopeIO.launch {
            getFoldersUseCase().collect {
                _models.value = it
            }
        }
    }

    override fun onFolderSelected(uri: Uri?) {
        uri?.let {
            Log.d("folders component folder_uri", uri.path!!)

            RefreshingStates.isAddingNewFolder = true

            addFolder(uri)
        }
    }

    override fun onFolderRemoveButtonClick(rootFolderEntity: RootFolderEntity) {
        if (
            !RefreshingStates.isAutomaticallyRefreshing &&
            !RefreshingStates.isManuallyRefreshing &&
            !RefreshingStates.isAddingNewFolder
        ) {
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                deleteFolderUseCase(rootFolderEntity)
                deleteAllBooksInFolderUseCase(rootFolderEntity.uri)
            }
        } else {
            Toast.makeText(
                context,
                context.getString(cat.petrushkacat.audiobookplayer.strings.R.string.not_available_during_scan),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBack() {
        onBackClicked()
    }

    private fun addFolder(folderUri: Uri) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            Log.d("folders component", "is adding = true")

            _foldersToProcess.value = 0
            _foldersProcessed.value = 0

            val newFolder = RootFolderEntity(
                uri = folderUri.toString(),
                name = folderUri.lastPathSegment!!,
                isCurrent = false
            )
            try {
                val contentResolver = context.contentResolver

                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(folderUri, takeFlags)
            } catch(e: Exception) {
                e.printStackTrace()
                Log.d("folders", "uri permission not granted")
            }

            addFolderUseCase(newFolder)
            parseBooks(folderUri)
        }

    }

    private fun parseBooks(folderUri: Uri) {
        val file = DocumentFile.fromTreeUri(context, folderUri)!!
        parseCycle(file, folderUri, Mutex())
    }

    private fun parseCycle(bookFolder: DocumentFile, rootFolderUri: Uri, mutex: Mutex) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            globalMutex.withLock {
                _foldersToProcess.value += 1
            }

            var name: String? = null
            var image: ByteArray? = null
            var bookDuration: Long = 0
            val chapters: MutableList<Chapter> = mutableListOf()
            val jobs = mutableListOf<Job>()

            bookFolder.listFiles().forEachIndexed { index, content ->
                if (content == null) return@forEachIndexed

                if (content.isDirectory) {
                    parseCycle(content, rootFolderUri, Mutex())
                }

                if (content.isAudio()) {
                    jobs.add(launch Chapter@{
                        val mmr = MediaMetadataRetriever()

                        //empty audio file crashes the app
                        try {
                            mmr.setDataSource(context, content.uri)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return@Chapter
                        }

                        name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                            ?: bookFolder.name!!

                        val chapterDuration =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                ?.toLong()!!
                        bookDuration += chapterDuration

                        if (image == null) {
                            image = mmr.embeddedPicture
                        }

                        mmr.release()

                        mutex.withLock {
                            chapters.add(
                                Chapter(
                                    bookFolder.uri.toString(),
                                    content.name?.substringBeforeLast('.')
                                        ?: "Chapter ${index + 1}",
                                    chapterDuration,
                                    bookDuration - chapterDuration,
                                    content.uri.toString()
                                )
                            )
                        }
                    })
                }
            }
            jobs.joinAll()

            name?.let {

                val sortedChapters = withContext(Dispatchers.Default) {
                    val temp = chapters.sortedWith { a, b ->
                        Log.d(name, extractInt(a).toString())
                        extractInt(a) - extractInt(b)
                    }

                    var timeFromBeginning = 0L
                    temp.forEach {
                        it.timeFromBeginning = timeFromBeginning
                        timeFromBeginning += it.duration
                    }
                    temp
                }

                saveBookUseCase(
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
                        addedTime = Date().time
                    )
                )
            }
            globalMutex.withLock {
                _foldersProcessed.value += 1
                if(_foldersProcessed.value == _foldersToProcess.value) {
                    RefreshingStates.isAddingNewFolder = false
                    Log.d("folders component", "is adding = false")
                }
            }
        }
    }

    companion object {
        private val _foldersToProcess = MutableStateFlow(0)
        private val _foldersProcessed = MutableStateFlow(0)
        private val globalMutex = Mutex()
    }
}