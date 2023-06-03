package cat.petrushkacat.audiobookplayer.components.components.main

import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.BookComponent
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.BookshelfComponent
import cat.petrushkacat.audiobookplayer.components.components.main.completedbooks.CompletedBooksComponent
import cat.petrushkacat.audiobookplayer.components.components.main.favorites.FavoritesComponent
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.FoldersComponent
import cat.petrushkacat.audiobookplayer.components.components.main.listenlater.ListenLaterComponent
import cat.petrushkacat.audiobookplayer.components.components.main.settings.SettingsComponent
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.StatisticsComponent
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface MainComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Bookshelf(val component: BookshelfComponent): Child
        data class Book(val component: BookComponent): Child
        data class Folder(val component: FoldersComponent): Child
        data class Settings(val component: SettingsComponent): Child
        data class ListenLater(val component: ListenLaterComponent): Child
        data class Favorites(val component: FavoritesComponent): Child
        data class CompletedBooks(val component: CompletedBooksComponent): Child
        data class Statistics(val component: StatisticsComponent): Child
    }
}