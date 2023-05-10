package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer.DrawerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.BookshelfToolbarComponent
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.MutableStateFlow

interface BookshelfComponent {

    val bookshelfToolbarComponent: BookshelfToolbarComponent

    val booksListComponent: BooksListComponent

    val drawerComponent: DrawerComponent

    val folder: MutableStateFlow<MutableList<RootFolderEntity>>

}