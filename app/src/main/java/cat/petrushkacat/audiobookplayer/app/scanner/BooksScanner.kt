package cat.petrushkacat.audiobookplayer.app.scanner

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.extractInt
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.isAudio
import cat.petrushkacat.audiobookplayer.components.states.RefreshingStates
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import cat.petrushkacat.audiobookplayer.domain.models.Chapter
import cat.petrushkacat.audiobookplayer.domain.models.Chapters
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUrisUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.SaveBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Date

class BooksScanner(
    private val context: Context,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val getBooksUrisUseCase: GetBooksUrisUseCase,
    private val deleteBookUseCase: DeleteBookUseCase
) {

    fun startScan() {
        if(
            !isScanning &&
            !RefreshingStates.isAddingNewFolder &&
            !RefreshingStates.isManuallyRefreshing
        ) {
            scan()
        } else {
            Log.d("scanner", "scan is not started")
        }
    }

    private fun scan() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            Log.d("scanner", "scan is started")
            started = Date().time
            RefreshingStates.isAutomaticallyRefreshing = true
            isScanning = true
            booksUris.clear()
            RefreshingStates.autoRefreshPercent = 0

            val folders = getFoldersUseCase().first()

            val folderJobs = mutableListOf<Job>()
            folders.forEach {
                folderJobs.add(launch {
                    val folder = DocumentFile.fromTreeUri(context, Uri.parse(it.uri))
                    try {
                        parseBooksUris(folder!!, it.uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
            }

            val savedUris = getBooksUrisUseCase().first()

            folderJobs.joinAll()

            Log.d("auto refresh filter", "uris")
            val parseBooksUris = booksUris.filter { bookUri ->
                Uri.parse(bookUri.folderUri) !in savedUris.map { Uri.parse(it.folderUri) }
            }

            val urisToDelete = savedUris.filter { bookUri ->
                Uri.parse(bookUri.folderUri) !in booksUris.map { Uri.parse(it.folderUri) }
            }

            Log.d("auto refresh db uris", savedUris.map { Uri.parse(it.folderUri) }.toString())
            Log.d("auto refresh save", parseBooksUris.map { Uri.parse(it.folderUri) }.toString())
            Log.d("auto refresh delete", urisToDelete.map { it.toString() }.toString())

            val jobs = mutableListOf<Job>()

            val newFolders = getFoldersUseCase.invoke().first()
            jobs.add(launch {
                parseBooksUris.forEach { bookUri ->
                    if (bookUri.rootFolderUri in newFolders.map { it.uri }) {
                        parseBooks(Uri.parse(bookUri.folderUri), bookUri.rootFolderUri)
                    }
                }
            })

            jobs.add(launch {
                urisToDelete.forEach {
                    deleteBookUseCase(it.folderUri)
                }
            })

            jobs.joinAll()
            if (parseBooksUris.isEmpty()) {
                RefreshingStates.isAutomaticallyRefreshing = false
                isScanning = false
                Log.d("scanner auto empty refresh time", "time: ${Date().time - started}")
            }
        }
    }

    private suspend fun parseBooksUris(bookFolder: DocumentFile, rootFolderUri: String) {
        globalMutex.withLock {
            booksUris.add(BookUri(bookFolder.uri.toString(), rootFolderUri))
        }
        val jobs = mutableListOf<Job>()
        bookFolder.listFiles().forEach { file ->
            if(file.isDirectory) {
                jobs.add(CoroutineScope(Job() + Dispatchers.IO).launch {
                    parseBooksUris(file, rootFolderUri)
                })
            }
        }
        Log.d("auto", jobs.size.toString())
        jobs.joinAll()
    }

    private fun parseBooks(fileUri: Uri, rootFolderUri: String) {
        val file = DocumentFile.fromTreeUri(context, fileUri)!!
        parseCycle(file, rootFolderUri, Mutex())

    }

    private fun parseCycle(bookFolder: DocumentFile, rootFolderUri: String, chaptersMutex: Mutex) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            globalMutex.withLock {
                foldersToProcess += 1
            }
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

                        chaptersMutex.withLock {
                            bookDuration += chapterDuration
                        }

                        if (image == null) {
                            image = mmr.embeddedPicture
                        }

                        mmr.release()

                        chaptersMutex.withLock {
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
                        rootFolderUri = rootFolderUri,
                        image = image,
                        addedTime = Date().time
                    )
                )
            }

            globalMutex.withLock {
                foldersProcessed += 1
                RefreshingStates.autoRefreshPercent = ((foldersProcessed.toFloat() / foldersToProcess) * 100).toInt()
                if(foldersProcessed == foldersToProcess) {
                    RefreshingStates.isAutomaticallyRefreshing = false
                    isScanning = false
                    foldersProcessed = 0
                    foldersToProcess = 0
                    Log.d("scanner auto refresh time", "time: ${Date().time - started}")
                }
            }
        }
    }

    companion object {
        private var foldersToProcess = 0
        private var foldersProcessed = 0
        private var isScanning = false
        private val globalMutex = Mutex()
        private val booksUris = mutableListOf<BookUri>()
        var started = Date().time
    }
}