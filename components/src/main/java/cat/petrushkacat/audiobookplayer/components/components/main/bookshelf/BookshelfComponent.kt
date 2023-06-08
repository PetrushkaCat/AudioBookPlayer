package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf

import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.books_scanner.BooksScannerComponent
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer.DrawerComponent
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.toolbar.BookshelfToolbarComponent

interface BookshelfComponent {

    val bookshelfToolbarComponent: BookshelfToolbarComponent

    val booksListComponent: BooksListComponent

    val drawerComponent: DrawerComponent

    val booksScannerComponent: BooksScannerComponent

}