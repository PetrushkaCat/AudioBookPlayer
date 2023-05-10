package cat.petrushkacat.audiobookplayer.core.components.main

import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponent
import cat.petrushkacat.audiobookplayer.core.components.main.folderselector.FoldersComponent
import cat.petrushkacat.audiobookplayer.core.components.main.settings.SettingsComponent
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface MainComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Bookshelf(val component: BookshelfComponent): Child
        data class Book(val component: BookComponent): Child
        data class Folder(val component: FoldersComponent): Child

        data class Settings(val component: SettingsComponent): Child
    }
}