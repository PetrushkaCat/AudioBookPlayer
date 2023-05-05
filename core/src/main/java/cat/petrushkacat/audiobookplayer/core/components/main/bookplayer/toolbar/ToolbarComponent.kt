package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.toolbar

import kotlinx.coroutines.flow.StateFlow

interface ToolbarComponent {

    val models: StateFlow<Model>
    fun onPlaySpeedChange(speed: Double)

    data class Model(
        val playSpeed: Double,
    )
}