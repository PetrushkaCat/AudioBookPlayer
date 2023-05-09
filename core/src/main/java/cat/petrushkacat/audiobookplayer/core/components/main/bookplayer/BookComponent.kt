package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer

import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookPlayerContainerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponent
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface BookComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class BookPlayerContainer(val component: BookPlayerContainerComponent): Child

        data class Notes(val component: NotesComponent): Child
    }

}