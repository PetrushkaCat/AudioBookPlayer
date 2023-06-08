package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cat.petrushkacat.audiobookplayer.components.components.main.MainComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation

@Composable
fun MainComponentUi(component: MainComponent) {
    val childStack by component.childStack.collectAsState()

    Children(
        stack = childStack,
        animation = stackAnimation { child ->
            when(child.instance) {
                is MainComponent.Child.Folder -> fade() + scale()
                else -> fade() + slide()
            }
        }
    ) { child ->
        when(val instance = child.instance) {
            is MainComponent.Child.Bookshelf -> BookshelfComponentUi(component = instance.component)
            is MainComponent.Child.Book -> BookComponentUi(component = instance.component)
            is MainComponent.Child.Folder -> FoldersComponentUi(component = instance.component)
            is MainComponent.Child.Settings -> SettingsComponentUi(component = instance.component)
            is MainComponent.Child.Favorites -> FavoritesComponentUi(component = instance.component)
            is MainComponent.Child.ListenLater -> ListenLaterComponentUi(component = instance.component)
            is MainComponent.Child.CompletedBooks -> CompletedBooksComponentUi(component = instance.component)
            is MainComponent.Child.Statistics -> StatisticsComponentUi(component = instance.component)
        }
    }
}