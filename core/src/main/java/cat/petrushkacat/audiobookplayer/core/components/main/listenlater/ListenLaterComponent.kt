package cat.petrushkacat.audiobookplayer.core.components.main.listenlater

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface ListenLaterComponent {

    val bookDropDownMenuComponent: BookDropdownMenuComponent

    val models: StateFlow<List<BooksListComponent.Model>>

    val settings: StateFlow<SettingsEntity>

    fun onBack()

    fun onBookClick(bookUri: String)

}