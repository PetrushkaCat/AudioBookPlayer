package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar

import android.net.Uri
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.ManualSleepTimerState
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeMain
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookPlayerToolbarComponentImpl(
    componentContext: ComponentContext,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val audiobooksRepository: AudiobooksRepository,
    private val settingsRepository: SettingsRepository,
    private val bookUri: Uri,
    private val onNotesButtonClicked: () -> Unit,
    private val onBackClicked: () -> Unit
) : BookPlayerToolbarComponent, ComponentContext by componentContext {

    private val _settings = MutableStateFlow(SettingsEntity())
    override val settings: StateFlow<SettingsEntity> = _settings.asStateFlow()

    override val manualSleepTimerState: StateFlow<ManualSleepTimerState> = audiobookServiceHandler.manualSleepTimerState

    override fun getPlaySpeed() = audiobookServiceHandler.getPlaySpeed()

    private val scopeMain = componentCoroutineScopeMain()
    private val scopeDefault = componentCoroutineScopeDefault()
    private val scopeIO = componentCoroutineScopeIO()


    init {
        scopeDefault.launch {
            settingsRepository.getSettings().collect {
                _settings.value = it
            }
        }
    }

    override fun onPlaySpeedChange(speed: Float) {
        scopeMain.launch {
            audiobookServiceHandler.setPlaySpeed(speed)
        }
    }

    override fun onNotesButtonClick() {
        onNotesButtonClicked()
    }

    override fun onBack() {
        onBackClicked()
    }

    override fun onManualSleep(sleepTimerType: SettingsEntity.SleepTimerType, haveToStopIfPlaying: Boolean) {
        audiobookServiceHandler.startStopManualSleepTimer(sleepTimerType, haveToStopIfPlaying)
        saveLastManualSleepDuration(sleepTimerType)
    }

    private fun saveLastManualSleepDuration(sleepTimerType: SettingsEntity.SleepTimerType) {
        scopeIO.launch {
            settingsRepository.saveSettings(
                settings.value.copy(
                    sleepTimerType = sleepTimerType
                )
            )
        }
    }
}
