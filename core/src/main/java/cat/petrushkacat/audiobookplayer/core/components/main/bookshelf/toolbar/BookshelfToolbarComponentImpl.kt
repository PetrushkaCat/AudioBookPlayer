package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.core.models.Grid
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfToolbarComponentImpl(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository,
    private val onFolderButtonClicked: () -> Unit,
    private val onSearched: (String) -> Unit
) : BookshelfToolbarComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()

    private val _settings = MutableStateFlow(SettingsEntity())
    override val settings: StateFlow<SettingsEntity>  = _settings.asStateFlow()

    init {
        scope.launch {
            settingsRepository.getSettings().collect {
                _settings.value = it
            }
        }
    }
    override fun onFolderButtonClick() {
        onFolderButtonClicked()
    }

    override fun onGridButtonClick() {
        scope.launch {
            var index = settings.value.grid.ordinal
            index = if (index + 1 == Grid.values().size) 0 else ++index
            settingsRepository.saveSettings(
                SettingsEntity(
                    id = settings.value.id,
                    versionCode = settings.value.versionCode,
                    autoSleepTime = settings.value.autoSleepTime,
                    rewindTime = settings.value.rewindTime,
                    autoRewindBackTime = settings.value.autoRewindBackTime,
                    theme = settings.value.theme,
                    grid = Grid.values()[index]
                )
            )
        }
    }

    override fun onSearch(text: String) {
        onSearched(text)
    }

}