package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.books_scanner

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.extractInt
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.isAudio
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import cat.petrushkacat.audiobookplayer.domain.models.Chapter
import cat.petrushkacat.audiobookplayer.domain.models.Chapters
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUrisUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.SaveBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Date

class BooksScannerComponentImpl(
    componentComponent: ComponentContext,
    private val context: Context,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val getBooksUrisUseCase: GetBooksUrisUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) : BooksScannerComponent, ComponentContext by componentComponent {

    private val scopeDefault = componentCoroutineScopeDefault()
    private val booksUris = mutableListOf<BookUri>()

    init {
        scopeDefault.launch {
            launch {
                lifecycle.doOnCreate {
                    //_scanState.value = BooksScannerComponent.ScanState.Scanning
                    scan()
                }
            }
        }
    }

    //private val _scanState = MutableStateFlow<BooksScannerComponent.ScanState>(BooksScannerComponent.ScanState.NotScanning)
    //override val scanState: StateFlow<BooksScannerComponent.ScanState> = _scanState.asStateFlow()
    private val globalMutex = Mutex()

    override fun scan() {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            val started = Date().time
            val folders = getFoldersUseCase.invoke().first()

            folders.forEach {
                val folder = DocumentFile.fromTreeUri(context, Uri.parse(it.uri))
                try {
                    parseBooksUris(folder!!, it.uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val savedUris = getBooksUrisUseCase().first()

            val parseBooksUris = booksUris.filter { bookUri ->
                Uri.parse(bookUri.folderUri) !in savedUris.map {Uri.parse(it.folderUri)}
            }

            val urisToDelete = savedUris
                .filter { bookUri ->
                    Uri.parse(bookUri.folderUri) !in booksUris.map {Uri.parse(it.folderUri)}
                }

            Log.d("auto refresh db uris", savedUris.map { Uri.parse(it.folderUri) }.toString())
            Log.d("auto refresh save", parseBooksUris.map { Uri.parse(it.folderUri) }.toString())
            Log.d("auto refresh delete", urisToDelete.map { it.toString() }.toString())

            val jobs = mutableListOf<Job>()

            jobs.add(launch {
                parseBooksUris.forEach {
                    parseBooks(Uri.parse(it.folderUri), it.rootFolderUri)
                }
            })

            jobs.add(launch {
                urisToDelete.forEach {
                    deleteBookUseCase(it.folderUri)
                }
            })

            jobs.joinAll()
            //_scanState.value = BooksScannerComponent.ScanState.NotScanning
            Log.d("auto refresh time", "time: ${Date().time - started}")
        }
    }

    private suspend fun parseBooksUris(bookFolder: DocumentFile, rootFolderUri: String) {
        globalMutex.withLock {
            booksUris.add(BookUri(bookFolder.uri.toString(), rootFolderUri))
        }

        bookFolder.listFiles().forEach { file ->
            if(file.isDirectory) {
                parseBooksUris(file, rootFolderUri)
            }
        }
    }

    private fun parseBooks(fileUri: Uri, rootFolderUri: String) {
        val file = DocumentFile.fromTreeUri(context, fileUri)!!
        parseCycle(file, rootFolderUri, Mutex())
    }

    private fun parseCycle(bookFolder: DocumentFile, rootFolderUri: String, mutex: Mutex) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {

            var name: String? = null
            var image: ByteArray? = null
            var bookDuration: Long = 0
            val chapters: MutableList<Chapter> = mutableListOf()
            val jobs = mutableListOf<Job>()

            bookFolder.listFiles().forEachIndexed { index, content ->
                if (content == null) return@forEachIndexed

                if (content.isDirectory) {
                    return@forEachIndexed
                }

                if (content.isAudio()) {
                    jobs.add(launch {
                        val mmr = MediaMetadataRetriever()
                        mmr.setDataSource(context, content.uri)
                        name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                            ?: bookFolder.name!!

                        val chapterDuration =
                            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                ?.toLong()!!
                        bookDuration += chapterDuration

                        if (image == null) {
                            image = mmr.embeddedPicture
                        }

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
                        rootFolderUri = rootFolderUri,
                        image = image,
                    )
                )
            }
        }
    }
}