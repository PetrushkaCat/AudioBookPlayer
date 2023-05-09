package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist

import android.net.Uri
import android.util.Log
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksListComponentImpl(
    componentContext: ComponentContext,
    val onBookSelected: (Uri) -> Unit,
    books: StateFlow<List<BooksListComponent.Model>>
) : BooksListComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()

    private val _models = MutableStateFlow<List<BooksListComponent.Model>>(emptyList())
    override val models: StateFlow<List<BooksListComponent.Model>> = _models

    init {
        scope.launch {
            val started: MutableList<BooksListComponent.Model> = mutableListOf()
            val completed: MutableList<BooksListComponent.Model> = mutableListOf()
            val notStarted: MutableList<BooksListComponent.Model> = mutableListOf()

            books.collect { list ->
                list.forEach {
                    if (it.isStarted && !it.isCompleted) {
                        started.add(it)
                    } else if (it.isCompleted) {
                        completed.add(it)
                    } else {
                        notStarted.add(it)
                    }
                }
                started.sortByDescending {
                    it.lastTimeListened
                }

                started += notStarted + completed
                _models.value = started.toMutableList()

                started.clear()
                completed.clear()
                notStarted.clear()
            }
        }
    }
    override fun onBookClick(uri: Uri) {
        onBookSelected(uri)
    }


}