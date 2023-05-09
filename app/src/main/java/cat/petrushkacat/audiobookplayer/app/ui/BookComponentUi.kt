package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.media3.common.util.UnstableApi
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@OptIn(ExperimentalMaterial3Api::class)
@UnstableApi
@Composable
fun BookComponentUi(component: BookComponent) {
    val childStack by component.childStack.collectAsState()

    Children(stack = childStack) { child ->
        when (val instance = child.instance) {
            is BookComponent.Child.BookPlayerContainer -> BookPlayerContainerComponentUi(component = instance.component)
            is BookComponent.Child.Notes -> NotesComponentUi(component = instance.component)
        }
    }
}