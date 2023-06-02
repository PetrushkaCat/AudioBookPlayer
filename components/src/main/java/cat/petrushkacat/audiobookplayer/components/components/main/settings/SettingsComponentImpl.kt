package cat.petrushkacat.audiobookplayer.components.components.main.settings

import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.Theme
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.SaveSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsComponentImpl(
    componentContext: ComponentContext,
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val onBackClicked: () -> Unit
) : SettingsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()
    private val scopeIO = componentCoroutineScopeIO()

    private val _models = MutableStateFlow(cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity())
    override val models: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity> = _models.asStateFlow()


    init {
        scope.launch {
            getSettingsUseCase().collect {
                _models.value = it
            }
        }
    }
    override fun saveSettings(settings: cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity) {
        scopeIO.launch {
            saveSettingsUseCase(settings)
        }
    }

    override fun changeTheme(isDark: Boolean) {
        scope.launch {
            saveSettings(
                models.value.copy(
                    theme = if (isDark) Theme.DARK else Theme.LIGHT
                )
            )
        }
    }

    override fun changeRewindTime(time: Long) {
        scope.launch {
            saveSettings(
                models.value.copy(
                    rewindTime = time
                )
            )
        }
    }

    override fun changeGreatRewindTime(time: Long) {
        saveSettings(
            models.value.copy(
                greatRewindTime = time
            )
        )
    }

    override fun changeAutoRewindTime(time: Long) {
        scope.launch {
            saveSettings(
                models.value.copy(
                    autoRewindBackTime = time
                )
            )
        }
    }

    override fun changeAutoSleepTime(time: Long) {
        scope.launch {
            saveSettings(
                models.value.copy(
                    autoSleepTime = time
                )
            )
        }
    }

    override fun onBack() {
        onBackClicked()
    }
}