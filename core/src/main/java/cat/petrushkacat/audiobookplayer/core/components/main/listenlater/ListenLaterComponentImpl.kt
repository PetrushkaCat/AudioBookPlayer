package cat.petrushkacat.audiobookplayer.core.components.main.listenlater

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropDownMenuComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropDownMenuComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListenLaterComponentImpl(
    componentContext: ComponentContext,
    private val audiobooksRepository: AudiobooksRepository,
    private val onBackPressed: () -> Unit,
    private val onBookClicked: (bookUri: String) -> Unit
) : ListenLaterComponent, ComponentContext by componentContext {

    override val bookDropDownMenuComponent: BookDropDownMenuComponent = BookDropDownMenuComponentImpl(
        childContext("listenLater_drop_down"),
        audiobooksRepository
    )

    private val scope = componentCoroutineScopeDefault()

    private val _models = MutableStateFlow<List<BooksListComponent.Model>>(emptyList())
    override val models = _models.asStateFlow()

    init {
        scope.launch {
            audiobooksRepository.getAllBooks().collect { books ->
                _models.value = books.filter { book ->
                    book.isWantToListen
                }
            }
        }
    }

    override fun onBack() {
        onBackPressed()
    }

    override fun onBookClick(bookUri: String) {
        onBookClicked(bookUri)
    }
}