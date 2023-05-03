package cat.petrushkacat.audiobookplayer.core.components.main

import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponent
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface MainComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Bookshelf(val component: BookshelfComponent): Child
        data class BookPlayer(val component: BookPlayerComponent): Child
    }
}