package cat.petrushkacat.audiobookplayer.core.components

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RootComponentImpl : RootComponent {
    override val models: StateFlow<RootComponent.A> = MutableStateFlow(RootComponent.A("1"))

}