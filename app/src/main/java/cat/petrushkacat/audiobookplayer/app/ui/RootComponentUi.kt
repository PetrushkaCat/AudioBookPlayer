package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.media3.common.util.UnstableApi
import cat.petrushkacat.audiobookplayer.core.components.RootComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@UnstableApi
@Composable
fun RootComponentUi(component: RootComponent) {
    val stack by component.childStack.collectAsState()

    Children(stack = stack) {
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