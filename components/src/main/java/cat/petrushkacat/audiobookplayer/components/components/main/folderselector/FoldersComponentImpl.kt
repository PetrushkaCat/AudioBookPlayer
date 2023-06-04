package cat.petrushkacat.audiobookplayer.components.components.main.folderselector

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.components.util.supportedAudioFormats
import cat.petrushkacat.audiobookplayer.components.util.supportedImageFormats
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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    private val scopeDefault = componentCoroutineScopeDefault()
    private val scopeIO = componentCoroutineScopeIO()

    private val _models: MutableStateFlow<List<RootFolderEntity>> = MutableStateFlow(emptyList())
    override val models: StateFlow<List<RootFolderEntity>> = _models.asStateFlow()

    override val foldersToProcess = _foldersToProcess.asStateFlow()
    override val foldersProcessed = _foldersProcessed.asStateFlow()

    init {
        scopeDefault.launch {
            getFoldersUseCase().collect {
                _models.value = it
            }
        }
    }

    override fun onFolderSelected(uri: Uri?) {
        uri?.let {
            Log.d("folder_uri", uri.path!!)
            addFolder(uri)
        }
    }

    override fun onFolderRemoveButtonClick(rootFolderEntity: RootFolderEntity) {
        scopeDefault.launch {
            deleteFolderUseCase(rootFolderEntity)
            deleteAllBooksInFolderUseCase(rootFolderEntity.uri)
        }
    }

    override fun onBack() {
        onBackClicked()
    }

    private fun addFolder(folderUri: Uri) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
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
        parseCycle(file, folderUri)
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
                )
                )
            }
            _foldersProcessed.value += 1
            //Log.d("folder5.6", booksToSave.toString())
        }
    }

    companion object {
        private val _foldersToProcess = MutableStateFlow(0)
        private val _foldersProcessed = MutableStateFlow(0)
    }
}

fun extractInt(chapter: Chapter): Int {
    val num = chapter.name.replace("\\D".toRegex(), "")
    // return 0 if no digits found
    return try {
        if (num.isEmpty()) 0 else Integer.parseInt(num)
    } catch (e: NumberFormatException) {
        Integer.parseInt(num.subSequence(num.length - 9, num.length).toString())
    }
}

fun DocumentFile.isAudio(): Boolean {
    if(!isFile) return false
    val name = name ?: return false
    if(name.substringAfterLast(".").lowercase() in supportedAudioFormats) return true
    return false
}

fun DocumentFile.isImage(): Boolean {
    if(!isFile) return false
    val name = name ?: return false
    if(name.substringAfterLast(".").lowercase() in supportedImageFormats ) return true
    return false
}