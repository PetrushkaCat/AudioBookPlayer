package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.bookslist.BooksListComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer.DrawerComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.toolbar.BookshelfToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import kotlinx.coroutines.flow.MutableStateFlow

class BookshelfComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    useCasesProvider: UseCasesProvider,
    onBookSelect: (Uri) -> Unit,
    onFolderButtonClick: () -> Unit,
    onSettingsClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onListenLaterClicked: () -> Unit,
    onCompletedBooksClicked: () -> Unit,
    onStatisticsClicked: () -> Unit
) : BookshelfComponent, ComponentContext by componentContext {

    private val searchText = MutableStateFlow("")

    override val bookshelfToolbarComponent = BookshelfToolbarComponentImpl(
        childContext("toolbar_component"),
        useCasesProvider.settingsUseCases.getSettingsUseCase,
        useCasesProvider.foldersUseCases.getFoldersUseCase,
        useCasesProvider.foldersUseCases.updateFolderUseCase,
        useCasesProvider.settingsUseCases.saveSettingsUseCase,
        onFolderButtonClicked = onFolderButtonClick,
        onSearched = {
            searchText.value = it
        }
    )

    override val booksListComponent = BooksListComponentImpl(
        childContext("books_list_component"),
        context,
        useCasesProvider.booksUseCases.getBookUseCase,
        useCasesProvider.booksUseCases.updateBookUseCase,
        useCasesProvider.booksUseCases.deleteIfNoInListUseCase,
        useCasesProvider.booksUseCases.saveBookUseCase,
        useCasesProvider.booksUseCases.getBooksUseCase,
        useCasesProvider.booksUseCases.getSearchedBooksUseCase,
        useCasesProvider.settingsUseCases.getSettingsUseCase,
        useCasesProvider.foldersUseCases.getFoldersUseCase,
        searchText,
        onBookSelected = onBookSelect,
    )

    override val drawerComponent = DrawerComponentImpl(
        childContext("drawer_component"),
        context,
        onSettingsClicked = onSettingsClicked,
        onFavoritesClicked = onFavoritesClicked,
        onListenLaterClicked = onListenLaterClicked,
        onCompletedBooksClicked = onCompletedBooksClicked,
        onStatisticsClicked = onStatisticsClicked
    )

}