package cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu

import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookDropdownMenuComponentImpl(
    componentContext: ComponentContext,
    private val getBookUseCase: GetBookUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
) : BookDropdownMenuComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()

    private val _selectedBook = MutableStateFlow(BookListEntity(name = "", folderUri = ""))
    override val selectedBook = _selectedBook.asStateFlow()

    override fun onBookDropDownEvent(
        bookUri: String,
        event: BookDropdownMenuComponent.BookDropDownEvent
    ) {
        scopeIO.launch {
            val book = getBookUseCase(bookUri)
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

    override fun selectBook(book: BookListEntity) {
        _selectedBook.value = book
    }

    private suspend fun bookNameChange(book: BookEntity, newName: String) {
        updateBookUseCase(book.copy(name = newName))
    }

    private suspend fun bookCoverChange(book: BookEntity, newCover: ByteArray) {
        updateBookUseCase(book.copy(image = newCover))
    }

    private suspend fun markOrUnmarkAsCompleted(book: BookEntity) {
        updateBookUseCase(book.copy(isCompleted = !book.isCompleted))
    }

    private suspend fun favoriteOrNotFavorite(book: BookEntity) {
        updateBookUseCase(book.copy(isFavorite = !book.isFavorite))
    }

    private suspend fun moveToOrOutListenLater(book: BookEntity) {
        updateBookUseCase(book.copy(isWantToListen = !book.isWantToListen))
    }

    private suspend fun moveToOrOutTrashBin(book: BookEntity) {
        updateBookUseCase(book.copy(isTemporarilyDeleted = !book.isTemporarilyDeleted))
    }
}