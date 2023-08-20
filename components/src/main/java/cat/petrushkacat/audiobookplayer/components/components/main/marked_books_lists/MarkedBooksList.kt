package cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists

import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.components.models.BookListItem
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface MarkedBooksList {

    val bookDropDownMenuComponent: BookDropdownMenuComponent

    val models: StateFlow<List<BookListItem>>

    val settings: StateFlow<SettingsEntity>


    fun onBack()

    fun onBookClick(bookUri: String)
}