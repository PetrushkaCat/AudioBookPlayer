package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf

import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer.DrawerComponent
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.toolbar.BookshelfToolbarComponent
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import kotlinx.coroutines.flow.MutableStateFlow

interface BookshelfComponent {

    val bookshelfToolbarComponent: BookshelfToolbarComponent

    val booksListComponent: BooksListComponent

    val drawerComponent: DrawerComponent

    val folder: MutableStateFlow<MutableList<RootFolderEntity>>

}