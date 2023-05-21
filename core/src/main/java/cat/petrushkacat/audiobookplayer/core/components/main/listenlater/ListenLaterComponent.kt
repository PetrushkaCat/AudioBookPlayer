package cat.petrushkacat.audiobookplayer.core.components.main.listenlater

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropDownMenuComponent
import kotlinx.coroutines.flow.StateFlow

interface ListenLaterComponent {

    val bookDropDownMenuComponent: BookDropDownMenuComponent

    val models: StateFlow<List<BooksListComponent.Model>>

    fun onBack()

    fun onBookClick(bookUri: String)

}