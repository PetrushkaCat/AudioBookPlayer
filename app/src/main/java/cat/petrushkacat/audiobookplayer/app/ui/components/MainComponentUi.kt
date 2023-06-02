package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.media3.common.util.UnstableApi
import cat.petrushkacat.audiobookplayer.components.components.main.MainComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@UnstableApi
@Composable
fun MainComponentUi(component: MainComponent) {
    val childStack by component.childStack.collectAsState()

    Children(stack = childStack) { child ->
        when(val instance = child.instance) {
            is MainComponent.Child.Bookshelf -> BookshelfComponentUi(component = instance.component)
            is MainComponent.Child.Book -> BookComponentUi(component = instance.component)
            is MainComponent.Child.Folder -> FoldersComponentUi(component = instance.component)
            is MainComponent.Child.Settings -> SettingsComponentUi(component = instance.component)
            is MainComponent.Child.Favorites -> FavoritesComponentUi(component = instance.component)
            is MainComponent.Child.ListenLater -> ListenLaterComponentUi(component = instance.component)
            is MainComponent.Child.CompletedBooks -> CompletedBooksComponentUi(component = instance.component)
        }
    }
}