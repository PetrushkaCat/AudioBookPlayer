package cat.petrushkacat.audiobookplayer.core.components

import cat.petrushkacat.audiobookplayer.core.components.main.MainComponent
import cat.petrushkacat.audiobookplayer.core.components.splashscreen.SplashScreenComponent
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class Main(val component: MainComponent): Child

        data class Splash(val component: SplashScreenComponent): Child
    }
}