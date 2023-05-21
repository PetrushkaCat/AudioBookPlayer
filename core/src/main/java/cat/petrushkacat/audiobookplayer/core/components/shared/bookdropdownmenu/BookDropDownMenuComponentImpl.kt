package cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeIO
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookDropDownMenuComponentImpl(
    componentContext: ComponentContext,
    private val audiobooksRepository: AudiobooksRepository
) : BookDropDownMenuComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()

    private val _selectedBookUri = MutableStateFlow("")
    override val selectedBookUri = _selectedBookUri.asStateFlow()

    override fun onBookDropDownEvent(
        bookUri: String,
        event: BookDropDownMenuComponent.BookDropDownEvent
    ) {
        scopeIO.launch {
            val book = audiobooksRepository.getBook(Uri.parse(bookUri))
            when(event) {
                is BookDropDownMenuComponent.BookDropDownEvent.BookCoverChange -> {
                    bookCoverChange(book.first(), event.image)
                }
                is BookDropDownMenuComponent.BookDropDownEvent.BookNameChange -> {
                    bookNameChange(book.first(), event.name)
                }

                BookDropDownMenuComponent.BookDropDownEvent.FavoriteOrNotFavorite -> {
                    favoriteOrNotFavorite(book.first())
                }
                BookDropDownMenuComponent.BookDropDownEvent.MarkOrUnmarkAsRead -> {
                    markOrUnmarkAsRead(book.first())
                }
                BookDropDownMenuComponent.BookDropDownEvent.MoveToOrOutListenLater -> {
                    moveToOrOutListenLater(book.first())
                }
                BookDropDownMenuComponent.BookDropDownEvent.MoveToOrOutTrashBin -> {
                    moveToOrOutTrashBin(book.first())
                }
            }

        }
    }

    override fun selectBook(bookUri: String) {
        _selectedBookUri.value = bookUri
    }

    private suspend fun bookNameChange(book: BookEntity, newName: String) {
        audiobooksRepository.updateBook(book.copy(name = newName))
    }

    private suspend fun bookCoverChange(book: BookEntity, newCover: ByteArray) {
        audiobooksRepository.updateBook(book.copy(image = newCover))
    }

    private suspend fun markOrUnmarkAsRead(book: BookEntity) {
        audiobooksRepository.updateBook(book.copy(isCompleted = !book.isCompleted))
    }

    private suspend fun favoriteOrNotFavorite(book: BookEntity) {
        audiobooksRepository.updateBook(book.copy(isFavorite = !book.isFavorite))
    }

    private suspend fun moveToOrOutListenLater(book: BookEntity) {
        audiobooksRepository.updateBook(book.copy(isWantToListen = !book.isWantToListen))
    }

    private suspend fun moveToOrOutTrashBin(book: BookEntity) {
        audiobooksRepository.updateBook(book.copy(isTemporarilyDeleted = !book.isTemporarilyDeleted))
    }
}