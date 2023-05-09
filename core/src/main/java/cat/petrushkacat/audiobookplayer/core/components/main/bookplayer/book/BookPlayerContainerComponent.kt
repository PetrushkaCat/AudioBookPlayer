package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponent

interface BookPlayerContainerComponent {

    val bookPlayerToolbarComponent: BookPlayerToolbarComponent

    val bookPlayerComponent: BookPlayerComponent
}