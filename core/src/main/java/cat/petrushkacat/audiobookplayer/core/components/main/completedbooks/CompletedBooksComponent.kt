package cat.petrushkacat.audiobookplayer.core.components.main.completedbooks

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface CompletedBooksComponent {

    val bookDropDownMenuComponent: BookDropdownMenuComponent

    val models: StateFlow<List<BooksListComponent.Model>>

    val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>


    fun onBack()

    fun onBookClick(bookUri: String)
}