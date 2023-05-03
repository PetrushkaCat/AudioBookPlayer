package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector.FolderSelectorComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.Chapter
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.NonCancellable.join
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookshelfComponentImpl(
    componentContext: ComponentContext,
    val context: Context,
    private val rootFoldersRepository: RootFoldersRepository,
    private val audiobooksRepository: AudiobooksRepository,
    onBookSelect: (Uri) -> Unit
) : BookshelfComponent, ComponentContext by componentContext {

    val scope = componentContext.componentCoroutineScopeDefault()
    override val folder: MutableStateFlow<MutableList<RootFolderEntity>> =
        MutableStateFlow(
            mutableListOf()
        )

    private val books: MutableStateFlow<MutableList<BooksListComponent.Model>> =
        MutableStateFlow(mutableListOf())

    //private val booksToSave: MutableList<BookEntity> = mutableListOf()


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

    override val toolbarComponent = ToolbarComponentImpl(
        childContext("toolbar_component"),
        {
            addFolder(it)
        }
    )

    override val booksListComponent = BooksListComponentImpl(
        childContext("books_list_component"),
        onBookSelected = onBookSelect,
        books
    )

    private fun addFolder(folderUri: Uri) {
        scope.launch {
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
        scope.launch {
            var name: String? = null
            var imageUri: Uri? = null
            var bookDuration: Long = 0

            val chapters: MutableList<Chapter> = mutableListOf()

            val mmr = MediaMetadataRetriever()

            for (content in bookFolder.listFiles()) {
                if (content == null) break
                if (content.isDirectory) {
                    parseCycle(content, rootFolderUri)
                }
                if (content.isAudio()) {
                    mmr.setDataSource(context, content.uri)
                    name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: bookFolder.name!!
                    
                    val chapterDuration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()!!
                    bookDuration += chapterDuration

                    chapters.add(Chapter(bookFolder.uri.toString(), content.name!!, chapterDuration, content.uri.toString()))
                }
                if (content.isImage()) {
                    imageUri = content.uri
                }
            }

            Log.d("folder5", name ?: "null")
            name?.let {
                /*val temp = books.value.toMutableList()
                val book = BooksListComponent.Model(imageUri.toString(), name, bookFolder.uri.toString())
                if (!temp.contains(book)) {
                    temp.add(book)
                }
                books.value = temp*/

                /*booksToSave.add(
                    BookEntity(
                        folderUri = bookFolder.uri.toString(),
                        name = name,
                        chapters = Chapters(chapters),
                        currentChapter = -1,
                        currentChapterTime = -1,
                        currentTime = -1,
                        duration = bookDuration,
                        rootFolderUri = rootFolderUri.toString(),
                        imageUri = imageUri.toString()
                    )
                )*/

                val sortedChapters = chapters.sortedWith { a, b ->
                    extractInt(a) - extractInt(b)
                }

                audiobooksRepository.saveBookAfterParse(BookEntity(
                    folderUri = bookFolder.uri.toString(),
                    name = name,
                    chapters = Chapters(sortedChapters),
                    currentChapter = -1,
                    currentChapterTime = -1,
                    currentTime = -1,
                    duration = bookDuration,
                    rootFolderUri = rootFolderUri.toString(),
                    imageUri = imageUri.toString()
                ))
            }
            Log.d("folder5.5", books.value.toString())
            //Log.d("folder5.6", booksToSave.toString())
        }
    }
}

fun extractInt(chapter: Chapter): Int {
    val num = chapter.name.replace("\\D".toRegex(), "")
    // return 0 if no digits found
    return if (num.isEmpty()) 0 else Integer.parseInt(num)
}

fun DocumentFile.isAudio(): Boolean {
    if(!isFile) return false
    val name = name ?: return false
    if(name.substringAfterLast(".").lowercase() == "mp3") return true
    return false
}

fun DocumentFile.isImage(): Boolean {
    if(!isFile) return false
    val name = name ?: return false
    if(name.substringAfterLast(".").lowercase() == "jpg") return true
    return false
}