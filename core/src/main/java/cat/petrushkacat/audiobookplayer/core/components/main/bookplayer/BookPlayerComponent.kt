package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer

import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponent

interface BookPlayerComponent {

    val toolbarComponent: ToolbarComponent

    val bookComponent: BookComponent

}