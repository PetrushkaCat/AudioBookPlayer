package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeMain
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class BookshelfComponentImpl(
    componentContext: ComponentContext,
    val context: Context,
    onBookSelect: (Uri) -> Unit
) : BookshelfComponent, ComponentContext by componentContext {

    val scope = componentContext.componentCoroutineScopeDefault()
    val folder = MutableStateFlow<Uri>(Uri.EMPTY)
    private val books: MutableStateFlow<MutableList<BooksListComponent.Model>> =
        MutableStateFlow(mutableListOf())


    init {

    }
    override val toolbarComponent = ToolbarComponentImpl(
        childContext("toolbar_component"),
        {
            folder.value = it
            parseBooks(folder.value)
        }
    )

    override val booksListComponent = BooksListComponentImpl(
        childContext("books_list_component"),
        onBookSelected = onBookSelect,
        books
    )


    private fun parseBooks(uri: Uri) {
            val file = DocumentFile.fromTreeUri(context, uri)!!
            parseCycle(file)
            Log.d("folder6", books.value.toString())
    }

    private fun parseCycle(bookFolder: DocumentFile) {
        scope.launch {
            var name: String? = null
            var imageUri: Uri? = null

            for (content in bookFolder.listFiles()) {
                if (content == null) break
                if (content.isDirectory) {
                    parseCycle(content)
                }
                if (content.isAudio()) {
                    name = bookFolder.name!!
                }
                if (content.isImage()) {
                    imageUri = content.uri
                }
            }

            Log.d("folder5", name ?: "null")
            name?.let {
                if (imageUri != null) {
                    val temp = books.value.toMutableList()
                    temp.add(BooksListComponent.Model(imageUri, name, bookFolder.uri))
                    books.value = temp
                } else {
                    val temp = books.value.toMutableList()
                    temp.add(BooksListComponent.Model(null, name, bookFolder.uri))
                    books.value = temp
                }
                Log.d("folder5.5", books.value.toString())
            }
        }
    }
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