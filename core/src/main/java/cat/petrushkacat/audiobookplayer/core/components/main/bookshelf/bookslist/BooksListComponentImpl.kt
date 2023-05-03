package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist

import android.net.Uri
import android.util.Log
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BooksListComponentImpl(
    componentContext: ComponentContext,
    val onBookSelected: (Uri) -> Unit,
    books: StateFlow<List<BooksListComponent.Model>>
) : BooksListComponent, ComponentContext by componentContext {

    override val models: StateFlow<List<BooksListComponent.Model>> = books

    override fun onBookClick(uri: Uri) {
        onBookSelected(uri)
    }


}