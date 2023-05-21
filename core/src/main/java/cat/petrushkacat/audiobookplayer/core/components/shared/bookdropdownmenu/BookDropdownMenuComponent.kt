package cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import kotlinx.coroutines.flow.StateFlow

interface BookDropdownMenuComponent {

    val selectedBook: StateFlow<BooksListComponent.Model>

    fun onBookDropDownEvent(bookUri: String, event: BookDropDownEvent)

    fun selectBook(book: BooksListComponent.Model)

    sealed interface BookDropDownEvent {

        data class BookNameChange(val name: String): BookDropDownEvent

        data class BookCoverChange(val image: ByteArray): BookDropDownEvent

        object FavoriteOrNotFavorite: BookDropDownEvent

        object MoveToOrOutTrashBin: BookDropDownEvent

        object MoveToOrOutListenLater: BookDropDownEvent

        object MarkOrUnmarkAsCompleted: BookDropDownEvent
    }
}
