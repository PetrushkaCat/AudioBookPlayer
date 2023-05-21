package cat.petrushkacat.audiobookplayer.core.components.main.favorites

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropDownMenuComponent
import kotlinx.coroutines.flow.StateFlow

interface FavoritesComponent {

    val bookDropDownMenuComponent: BookDropDownMenuComponent

    val models: StateFlow<List<BooksListComponent.Model>>

    fun onBack()

    fun onBookClick(bookUri: String)
}