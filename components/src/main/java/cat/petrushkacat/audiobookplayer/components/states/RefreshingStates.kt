package cat.petrushkacat.audiobookplayer.components.states

import kotlinx.coroutines.flow.MutableStateFlow

object RefreshingStates {
    val isAddingNewFolder = MutableStateFlow(false)
    val isManuallyRefreshing = MutableStateFlow(false)
    val isAutomaticallyRefreshing = MutableStateFlow(false)
}