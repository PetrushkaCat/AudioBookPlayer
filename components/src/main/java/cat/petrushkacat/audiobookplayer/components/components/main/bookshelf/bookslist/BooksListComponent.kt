package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.bookslist

import android.net.Uri
import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import kotlinx.coroutines.flow.StateFlow

interface BooksListComponent {

    val bookDropDownMenuComponent: BookDropdownMenuComponent

    val models: StateFlow<List<BookListEntity>>

    val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>

    val foldersToProcess: StateFlow<Int>
    val foldersProcessed: StateFlow<Int>
    val isRefreshing: StateFlow<Boolean>

    val isSearching: StateFlow<Boolean>

    val foldersCount: StateFlow<Int>
    fun onBookClick(uri: Uri)

    fun refresh()

}