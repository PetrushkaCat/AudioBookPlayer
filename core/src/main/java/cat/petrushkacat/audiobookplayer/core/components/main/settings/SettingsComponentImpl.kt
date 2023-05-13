package cat.petrushkacat.audiobookplayer.core.components.main.settings

import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.models.Theme
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsComponentImpl(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository
) : SettingsComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()

    private val _models = MutableStateFlow(SettingsEntity())
    override val models: StateFlow<SettingsEntity> = _models.asStateFlow()


    init {
        scope.launch {
            settingsRepository.getSettings().collect {
                _models.value = it
            }
        }
    }
    override fun saveSettings(settings: SettingsEntity) {
        settingsRepository.saveSettings(settings)
    }

    override fun changeTheme(isDark: Boolean) {
        scope.launch {
            settingsRepository.saveSettings(
                SettingsEntity(
                    id = models.value.id,
                    versionCode = models.value.versionCode,
                    autoSleepTime = models.value.autoSleepTime,
                    rewindTime = models.value.rewindTime,
                    autoRewindBackTime = models.value.autoRewindBackTime,
                    theme = if (isDark) Theme.DARK else Theme.LIGHT,
                    grid = models.value.grid
                )
            )
        }
    }

    override fun changeRewindTime(time: Long) {
        scope.launch {
            settingsRepository.saveSettings(
                SettingsEntity(
                    id = models.value.id,
                    versionCode = models.value.versionCode,
                    autoSleepTime = models.value.autoSleepTime,
                    rewindTime = time,
                    autoRewindBackTime = models.value.autoRewindBackTime,
                    theme = models.value.theme,
                    grid = models.value.grid
                )
            )
        }
    }

    override fun changeAutoRewindTime(time: Long) {
        scope.launch {
            settingsRepository.saveSettings(
                SettingsEntity(
                    id = models.value.id,
                    versionCode = models.value.versionCode,
                    autoSleepTime = models.value.autoSleepTime,
                    rewindTime = models.value.rewindTime,
                    autoRewindBackTime = time,
                    theme = models.value.theme,
                    grid = models.value.grid
                )
            )
        }
    }

    override fun changeAutoSleepTime(time: Long) {
        scope.launch {
            settingsRepository.saveSettings(
                SettingsEntity(
                    id = models.value.id,
                    versionCode = models.value.versionCode,
                    autoSleepTime = time,
                    rewindTime = models.value.rewindTime,
                    autoRewindBackTime = models.value.autoRewindBackTime,
                    theme = models.value.theme,
                    grid = models.value.grid
                )
            )
        }
    }
}