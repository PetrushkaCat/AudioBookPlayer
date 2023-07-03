package cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists.favorites

import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponentImpl
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesComponentImpl(
    componentContext: ComponentContext,
    private val getBookUseCase: GetBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val getBooksUseCase: GetBooksUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val onBackPressed: () -> Unit,
    private val onBookClicked: (bookUri: String) -> Unit
) : FavoritesComponent, ComponentContext by componentContext {

    override val bookDropDownMenuComponent: BookDropdownMenuComponent = BookDropdownMenuComponentImpl(
        childContext("favorites_drop_down"),
        getBookUseCase,
        updateBookUseCase
    )

    private val scopeIO = componentCoroutineScopeIO()

    private val _models = MutableStateFlow<List<BookListEntity>>(emptyList())
    override val models = _models.asStateFlow()

    private val _settings = MutableStateFlow(cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity())
    override val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity> = _settings.asStateFlow()


    init {
        scopeIO.launch {
            launch {
                getBooksUseCase(GetBooksUseCase.BooksType.Favorites).collect { books ->
                    _models.value = books
                }
            }
            launch {
                getSettingsUseCase().collect {
                    _settings.value = it
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