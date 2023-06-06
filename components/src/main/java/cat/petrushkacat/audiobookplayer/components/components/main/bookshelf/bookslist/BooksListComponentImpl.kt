package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.bookslist

import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.extractInt
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.isAudio
import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponentImpl
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.models.Chapter
import cat.petrushkacat.audiobookplayer.domain.models.Chapters
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteIfNoInListUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetSearchedBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.SaveBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BooksListComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val getBookUseCase: GetBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val deleteIfNoInListUseCase: DeleteIfNoInListUseCase,
    private val saveBookUseCase: SaveBookUseCase,
    private val getBooksUseCase: GetBooksUseCase,
    private val getSearchedBooksUseCase: GetSearchedBooksUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val searchText: StateFlow<String>,
    val onBookSelected: (Uri) -> Unit,
) : BooksListComponent, ComponentContext by componentContext {

    override val bookDropDownMenuComponent: BookDropdownMenuComponent = BookDropdownMenuComponentImpl(
        childContext("books_list_drop_down_menu"),
        getBookUseCase,
        updateBookUseCase
    )

    private val scopeIO = componentCoroutineScopeIO()
    private val scope = componentCoroutineScopeDefault()

    private val _models = MutableStateFlow<List<BookListEntity>>(emptyList())
    override val models: StateFlow<List<BookListEntity>> = _models

    private val _settings = MutableStateFlow(cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity())
    override val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity> = _settings.asStateFlow()

    override val foldersToProcess = _foldersToProcess.asStateFlow()
    override val foldersProcessed = _foldersProcessed.asStateFlow()
    override val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    override val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private var folderUris: List<Uri> = emptyList()

    init {
        scope.launch {
            launch {
                getSettingsUseCase().collect {
                    _settings.value = it
                }
            }
            launch {
                getBooksUseCase(GetBooksUseCase.BooksType.All).collect {
                    Log.d("books", "getBooks")
                    val temp = if(searchText.value.isNotEmpty()) {
                        getSearchedBooksUseCase(searchText.value, null).first()
                    } else if(folderUris.size == 1) {
                        getBooksUseCase(GetBooksUseCase.BooksType.Folder(folderUris[0].toString())).first()
                    } else {
                        getBooksUseCase(GetBooksUseCase.BooksType.All).first()
                    }
                    _models.value = temp.toMutableList()
                }
            }
            launch {
                getFoldersUseCase().collect { list ->
                    val selectedFolder = list.firstOrNull() {
                        it.isCurrent
                    }
                    delay(200)
                    folderUris = if(selectedFolder != null) {
                        _models.value = getBooksUseCase(GetBooksUseCase.BooksType.Folder(selectedFolder.uri)).first()
                        listOf(Uri.parse(selectedFolder.uri))
                    } else {
                        _models.value = getBooksUseCase(GetBooksUseCase.BooksType.All).first()
                        list.map { Uri.parse(it.uri) }
                    }
                }
            }
            launch {
                searchText.collect {
                    Log.d("search collect", it)
                    val books = getSearchedBooksUseCase(
                        it,
                        if(folderUris.size == 1) folderUris[0].toString() else null
                    ).first()
                    _isSearching.value = it.isNotEmpty()
                    _models.value = books
                }
            }
            launch {
                _foldersProcessed.collect {
                    if(it == _foldersToProcess.value && it > 0) {
                        Log.d("refreshing", bookUris.toString())
                        deleteIfNoInListUseCase(bookUris, refreshingFolderUris.map { uri -> uri.toString() })
                        _isRefreshing.value = false
                        _foldersProcessed.value = 0
                        _foldersToProcess.value = 0
                        bookUris.clear()
                        refreshingFolderUris = emptyList()
                    }
                }
            }
        }
    }
    override fun onBookClick(uri: Uri) {
        onBookSelected(uri)
    }

    override fun refresh() {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            refreshingFolderUris = folderUris
            _isRefreshing.value = true
            _foldersProcessed.value = 0
            _foldersToProcess.value = 0
            Log.d("refreshing", _isRefreshing.value.toString())
            Log.d("refreshing", _foldersProcessed.value.toString())
            Log.d("refreshing", _foldersToProcess.value.toString())

            for (uri in folderUris) {
                launch {
                    try {
                        val contentResolver = context.contentResolver

                        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        contentResolver.takePersistableUriPermission(uri, takeFlags)
                    } catch(e: Exception) {
                        e.printStackTrace()
                        Log.d("books list", "uri permission not granted")
                    }
                    parseBooks(uri)
                }
            }
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

                if(!isActive) return@launch
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
                bookUris.add(bookFolder.uri.toString())
                Log.d("refreshing", "saved")
            }
            _foldersProcessed.value += 1
        }
    }

    private fun filterBooks(folders: List<RootFolderEntity>, books: List<BookListEntity>): List<BookListEntity> {
        val currentFolder = folders.firstOrNull { it.isCurrent }
        return if(currentFolder != null) {
            books.filter {
                it.rootFolderUri == currentFolder.uri
            }
        } else {
            books.toMutableList()
        }
    }

    companion object {
        private val _foldersToProcess = MutableStateFlow(0)
        private val _foldersProcessed = MutableStateFlow(0)
        private val _isRefreshing = MutableStateFlow(false)
        private val bookUris: MutableList<String> = mutableListOf()
        private var refreshingFolderUris: List<Uri> = emptyList()
    }
}