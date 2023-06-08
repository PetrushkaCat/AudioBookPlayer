package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.BookComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookComponentUi(component: BookComponent) {
    val childStack by component.childStack.collectAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(fade() + scale())
    ) { child ->
        when (val instance = child.instance) {
            is BookComponent.Child.BookPlayerContainer -> BookPlayerContainerComponentUi(component = instance.component)
            is BookComponent.Child.Notes -> NotesComponentUi(component = instance.component)
        }
    }
}