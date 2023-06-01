package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar

import cat.petrushkacat.audiobookplayer.audioservice.ManualSleepTimerState
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface BookPlayerToolbarComponent {

    val settings: StateFlow<SettingsEntity>
    val manualSleepTimerState: StateFlow<ManualSleepTimerState>
    fun getPlaySpeed(): Float

    fun onPlaySpeedChange(speed: Float)

    fun onNotesButtonClick()

    fun onBack()

    fun onManualSleep(sleepTimerType: SettingsEntity.SleepTimerType, haveToStopIfPlaying: Boolean)

}