package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.isAudio
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val bookUri: Uri
) : BookComponent, ComponentContext by componentContext {

    private val scope = componentContext.componentCoroutineScopeDefault()

    override val models: MutableStateFlow<BookComponent.Model> = MutableStateFlow(BookComponent.Model(bookUri, "name", emptyList(), imageUri = null))

    init {
        parseBook()
    }

    private fun parseBook() {
        scope.launch {
            val file = DocumentFile.fromTreeUri(context, bookUri)
            val name = file!!.name!!
            val chapters = mutableListOf<BookComponent.Chapter>()
            for(chapter in file.listFiles()) {
                if(chapter.isAudio()) {
                    chapters.add(BookComponent.Chapter(chapter.name!!, chapter.uri))
                }
            }
            val sortedChapters = chapters.sortedWith { a, b ->
                extractInt(a) - extractInt(b)
            }
            models.value = BookComponent.Model(bookUri, name, sortedChapters, imageUri = null)
        }
    }

    fun extractInt(chapter: BookComponent.Chapter): Int {
        val num = chapter.name.replace("\\D".toRegex(), "")
        // return 0 if no digits found
        return if (num.isEmpty()) 0 else Integer.parseInt(num)
    }
}