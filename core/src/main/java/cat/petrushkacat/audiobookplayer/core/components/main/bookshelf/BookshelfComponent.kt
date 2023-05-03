package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf

import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponent

interface BookshelfComponent {

    val toolbarComponent: ToolbarComponent

    val booksListComponent: BooksListComponent
}