package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cat.petrushkacat.audiobookplayer.components.components.RootComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation

@Composable
fun RootComponentUi(component: RootComponent) {
    val stack by component.childStack.collectAsState()

    Children(
        stack = stack,
        animation = stackAnimation(fade() + scale())
    ) {
        when(val instance = it.instance) {
            is RootComponent.Child.Main -> {
                MainComponentUi(component = instance.component)
            }
            is RootComponent.Child.Splash -> {
                SplashScreenUi(component = instance.component)
            }
        }
    }
}