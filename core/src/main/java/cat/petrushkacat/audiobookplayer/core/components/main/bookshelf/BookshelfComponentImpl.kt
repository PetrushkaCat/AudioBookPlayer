package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponentImpl
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val rootFoldersRepository: RootFoldersRepository,
    private val audiobooksRepository: AudiobooksRepository,
    private val settingsRepository: SettingsRepository,
    onBookSelect: (Uri) -> Unit,
    onFolderButtonClick: () -> Unit,
    onSettingsClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onListenLaterClicked: () -> Unit,
    onCompletedBooksClicked: () -> Unit
    ) : BookshelfComponent, ComponentContext by componentContext {

    private val scope = componentContext.componentCoroutineScopeIO()

    override val folder: MutableStateFlow<MutableList<RootFolderEntity>> =
        MutableStateFlow(
            mutableListOf()
        )

    private val books: MutableStateFlow<MutableList<BooksListComponent.Model>> =
        MutableStateFlow(mutableListOf())

    private val searchedBooks: MutableStateFlow<List<BooksListComponent.Model>> =
        MutableStateFlow(mutableListOf())
    private val _isSearching = MutableStateFlow(false)


    init {
        scope.launch {
            rootFoldersRepository.getFolders().collect() {
                folder.value = it.toMutableList()
            }
        }
        scope.launch {
            audiobooksRepository.getAllBooks().collect() {
                books.value = it.toMutableList()
                searchedBooks.value = it
            }
        }
    }

    override val bookshelfToolbarComponent = BookshelfToolbarComponentImpl(
        childContext("toolbar_component"),
        settingsRepository,
        onFolderButtonClicked = onFolderButtonClick,
        onSearched = { text ->
            if(text.isNotEmpty()) {
                searchedBooks.value = books.value.filter { book ->
                    book.name.lowercase().contains(Regex(text.lowercase()))
                }
                _isSearching.value = true
            } else {
                searchedBooks.value = books.value
                _isSearching.value = false
                }
        }
    )

    override val booksListComponent = BooksListComponentImpl(
        childContext("books_list_component"),
        settingsRepository,
        audiobooksRepository,
        rootFoldersRepository,
        context,
        onBookSelected = onBookSelect,
        searchedBooks,
        _isSearching.asStateFlow()
    )

    override val drawerComponent = DrawerComponentImpl(
        childContext("drawer_component"),
        context,
        onSettingsClicked = onSettingsClicked,
        onFavoritesClicked = onFavoritesClicked,
        onListenLaterClicked = onListenLaterClicked,
        onCompletedBooksClicked = onCompletedBooksClicked
    )
}