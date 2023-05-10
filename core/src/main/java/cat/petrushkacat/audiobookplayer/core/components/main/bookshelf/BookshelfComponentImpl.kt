package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import android.content.Context
import android.content.Intent
import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer.DrawerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer.DrawerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.BookshelfToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeIO
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BookshelfComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val rootFoldersRepository: RootFoldersRepository,
    private val audiobooksRepository: AudiobooksRepository,
    private val settingsRepository: SettingsRepository,
    onBookSelect: (Uri) -> Unit,
    onFolderButtonClick: () -> Unit,
    onSettingsClicked: () ->Unit,

    ) : BookshelfComponent, ComponentContext by componentContext {

    private val scope = componentContext.componentCoroutineScopeIO()

    override val folder: MutableStateFlow<MutableList<RootFolderEntity>> =
        MutableStateFlow(
            mutableListOf()
        )

    private val books: MutableStateFlow<MutableList<BooksListComponent.Model>> =
        MutableStateFlow(mutableListOf())

    //private val booksToSave: MutableList<BookEntity> = mutableListOf()

    /*override val foldersToProcess = _foldersToProcess.asStateFlow()
    override val foldersProcessed = _foldersProcessed.asStateFlow()*/


    init {
        scope.launch {
            rootFoldersRepository.getFolders().collect() {
                folder.value = it.toMutableList()
            }
        }
        scope.launch {
            audiobooksRepository.getAllBooks().collect() {
                books.value = it.toMutableList()
            }
        }
    }

    override val bookshelfToolbarComponent = BookshelfToolbarComponentImpl(
        childContext("toolbar_component"),
        settingsRepository,
        onFolderButtonClicked = onFolderButtonClick
    )

    override val booksListComponent = BooksListComponentImpl(
        childContext("books_list_component"),
        settingsRepository,
        context,
        onBookSelected = onBookSelect,
        books
    )
    override val drawerComponent = DrawerComponentImpl(
        childContext("drawer_component"),
        onSettingsClicked = onSettingsClicked
    )
}

/*
    private fun addFolder(folderUri: Uri) {
        scope.launch {
            _foldersToProcess.value = 0
            _foldersProcessed.value = 0

            val file = DocumentFile.fromTreeUri(context, folderUri)!!
            val newFolder = RootFolderEntity(
                uri = folderUri.toString(),
                name = file.name!!,
                isCurrent = true
            )
            rootFoldersRepository.addFolder(newFolder)
            parseBooks(folderUri)
        }

    }

    private fun parseBooks(folderUri: Uri) {
        scope.launch {
            val file = DocumentFile.fromTreeUri(context, folderUri)!!
            parseCycle(file, folderUri)
            Log.d("folder6", books.value.toString())

            //audiobooksRepository.saveAfterParse(booksToSave)
        }
    }

    private fun parseCycle(bookFolder: DocumentFile, rootFolderUri: Uri) {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            _foldersToProcess.value += 1
            var name: String? = null
            var imageUri: Uri? = null
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

                    chapters.add(Chapter(bookFolder.uri.toString(), content.name?.substringBeforeLast('.') ?: "Chapter ${index + 1}", chapterDuration, content.uri.toString()))
                }
                if (content.isImage()) {
                    imageUri = content.uri
                }
            }

            name?.let {

                val sortedChapters = chapters.sortedWith { a, b ->
                    extractInt(a) - extractInt(b)
                }

                audiobooksRepository.saveBookAfterParse(BookEntity(
                    folderUri = bookFolder.uri.toString(),
                    folderName = bookFolder.name!!,
                    name = name!!,
                    chapters = Chapters(sortedChapters),
                    currentChapter = 0,
                    currentChapterTime = 0,
                    currentTime = 0,
                    duration = bookDuration,
                    rootFolderUri = rootFolderUri.toString(),
                    imageUri = imageUri.toString()
                ))
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
}*/
