package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.bookslist.BooksListComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer.DrawerComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.toolbar.BookshelfToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUseCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val useCasesProvider: UseCasesProvider,
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

    private val books: MutableStateFlow<MutableList<BookListEntity>> =
        MutableStateFlow(mutableListOf())

    private val searchedBooks: MutableStateFlow<List<BookListEntity>> =
        MutableStateFlow(mutableListOf())
    private val _isSearching = MutableStateFlow(false)


    init {
        scope.launch {
           useCasesProvider.foldersUseCases.getFoldersUseCase().collect() { folders ->
                folder.value = folders.toMutableList()
                filterBooks(folders)
            }
        }
        scope.launch {
            useCasesProvider.booksUseCases.getBooksUseCase(GetBooksUseCase.BooksType.All).collect() {
                books.value = it.toMutableList()
                filterBooks(folder.value)
            }
        }
    }

    override val bookshelfToolbarComponent = BookshelfToolbarComponentImpl(
        childContext("toolbar_component"),
        useCasesProvider.settingsUseCases.getSettingsUseCase,
        useCasesProvider.foldersUseCases.getFoldersUseCase,
        useCasesProvider.foldersUseCases.updateFolderUseCase,
        useCasesProvider.settingsUseCases.saveSettingsUseCase,
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
        context,
        useCasesProvider.booksUseCases.getBookUseCase,
        useCasesProvider.booksUseCases.updateBookUseCase,
        useCasesProvider.booksUseCases.deleteIfNoInListUseCase,
        useCasesProvider.booksUseCases.saveBookUseCase,
        useCasesProvider.settingsUseCases.getSettingsUseCase,
        useCasesProvider.foldersUseCases.getFoldersUseCase,
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

    private suspend fun filterBooks(folders: List<RootFolderEntity>) {
        val currentFolder = folders.firstOrNull { it.isCurrent }
        delay(500)
        if(currentFolder != null) {
            searchedBooks.value = books.value.filter {
                it.rootFolderUri == currentFolder.uri
            }
        } else {
            searchedBooks.value = books.value.toMutableList()
        }
    }
}