package cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu

import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import kotlinx.coroutines.flow.StateFlow

interface BookDropdownMenuComponent {

    val selectedBook: StateFlow<BookListEntity>

    fun onBookDropDownEvent(bookUri: String, event: BookDropDownEvent)

    fun selectBook(book: BookListEntity)

    sealed interface BookDropDownEvent {

        data class BookNameChange(val name: String): BookDropDownEvent

        data class BookCoverChange(val image: ByteArray): BookDropDownEvent

        object FavoriteOrNotFavorite: BookDropDownEvent

        object MoveToOrOutTrashBin: BookDropDownEvent

        object MoveToOrOutListenLater: BookDropDownEvent

        object MarkOrUnmarkAsCompleted: BookDropDownEvent
    }
}
