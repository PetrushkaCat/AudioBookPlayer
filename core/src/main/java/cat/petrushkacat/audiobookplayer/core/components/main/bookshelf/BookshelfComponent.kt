package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponent
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface BookshelfComponent {

    val toolbarComponent: ToolbarComponent

    val booksListComponent: BooksListComponent

    val folder: MutableStateFlow<MutableList<RootFolderEntity>>

    val foldersToProcess: StateFlow<Int>

    val foldersProcessed: StateFlow<Int>
}