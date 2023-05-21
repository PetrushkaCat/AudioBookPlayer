package cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeIO
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookDropdownMenuComponentImpl(
    componentContext: ComponentContext,
    private val audiobooksRepository: AudiobooksRepository
) : BookDropdownMenuComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()

    private val _selectedBook = MutableStateFlow(BooksListComponent.Model(name = "", folderUri = ""))
    override val selectedBook = _selectedBook.asStateFlow()

    override fun onBookDropDownEvent(
        bookUri: String,
        event: BookDropdownMenuComponent.BookDropDownEvent
    ) {
        scopeIO.launch {
            val book = audiobooksRepository.getBook(Uri.parse(bookUri))
            when(event) {
                is BookDropdownMenuComponent.BookDropDownEvent.BookCoverChange -> {
                    bookCoverChange(book.first(), event.image)
                }
                is BookDropdownMenuComponent.BookDropDownEvent.BookNameChange -> {
                    bookNameChange(book.first(), event.name)
                }

                BookDropdownMenuComponent.BookDropDownEvent.FavoriteOrNotFavorite -> {
                    favoriteOrNotFavorite(book.first())
                }
                BookDropdownMenuComponent.BookDropDownEvent.MarkOrUnmarkAsCompleted -> {
                    markOrUnmarkAsCompleted(book.first())
                }
                BookDropdownMenuComponent.BookDropDownEvent.MoveToOrOutListenLater -> {
                    moveToOrOutListenLater(book.first())
                }
                BookDropdownMenuComponent.BookDropDownEvent.MoveToOrOutTrashBin -> {
                    moveToOrOutTrashBin(book.first())
                }
            }

        }
    }

    override fun selectBook(book: BooksListComponent.Model) {
        _selectedBook.value = book
    }

    private suspend fun bookNameChange(book: BookEntity, newName: String) {
        audiobooksRepository.updateBook(book.copy(name = newName))
    }

    private suspend fun bookCoverChange(book: BookEntity, newCover: ByteArray) {
        audiobooksRepository.updateBook(book.copy(image = newCover))
    }

    private suspend fun markOrUnmarkAsCompleted(book: BookEntity) {
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