package cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu

import kotlinx.coroutines.flow.StateFlow

interface BookDropDownMenuComponent {

    val selectedBookUri: StateFlow<String>

    fun onBookDropDownEvent(bookUri: String, event: BookDropDownEvent)

    fun selectBook(bookUri: String)

    sealed interface BookDropDownEvent {

        data class BookNameChange(val name: String): BookDropDownEvent

        data class BookCoverChange(val image: ByteArray): BookDropDownEvent

        object FavoriteOrNotFavorite: BookDropDownEvent

        object MoveToOrOutTrashBin: BookDropDownEvent

        object MoveToOrOutListenLater: BookDropDownEvent

        object MarkOrUnmarkAsRead: BookDropDownEvent
    }
}
