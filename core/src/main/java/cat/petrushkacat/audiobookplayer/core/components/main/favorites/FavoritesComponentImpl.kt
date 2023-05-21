package cat.petrushkacat.audiobookplayer.core.components.main.favorites

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropdownMenuComponentImpl
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesComponentImpl(
    componentContext: ComponentContext,
    private val audiobooksRepository: AudiobooksRepository,
    private val settingsRepository: SettingsRepository,
    private val onBackPressed: () -> Unit,
    private val onBookClicked: (bookUri: String) -> Unit
) : FavoritesComponent, ComponentContext by componentContext {

    override val bookDropDownMenuComponent: BookDropdownMenuComponent = BookDropdownMenuComponentImpl(
        childContext("favorites_drop_down"),
        audiobooksRepository
    )

    private val scope = componentCoroutineScopeDefault()

    private val _models = MutableStateFlow<List<BooksListComponent.Model>>(emptyList())
    override val models = _models.asStateFlow()

    private val _settings = MutableStateFlow(SettingsEntity())
    override val settings: StateFlow<SettingsEntity> = _settings.asStateFlow()


    init {
        scope.launch {
            launch {
                audiobooksRepository.getAllBooks().collect { books ->
                    _models.value = books.filter { book ->
                        book.isFavorite
                    }
                }
            }
            launch {
                settingsRepository.getSettings().collect {
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