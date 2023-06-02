package cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.book.toolbar

import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.ManualSleepTimerState
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeMain
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.SaveSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookPlayerToolbarComponentImpl(
    componentContext: ComponentContext,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
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
            getSettingsUseCase().collect {
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
            saveSettingsUseCase(
                settings.value.copy(
                    sleepTimerType = sleepTimerType
                )
            )
        }
    }
}
