package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeIO
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfToolbarComponentImpl(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository,
    private val foldersRepository: RootFoldersRepository,
    private val onFolderButtonClicked: () -> Unit,
    private val onSearched: (String) -> Unit
) : BookshelfToolbarComponent, ComponentContext by componentContext {

    private val scope = componentCoroutineScopeDefault()
    private val scopeIO = componentCoroutineScopeIO()

    private val _settings = MutableStateFlow(cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity())
    override val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>  = _settings.asStateFlow()

    private val _folders = MutableStateFlow<List<RootFolderEntity>>(listOf(RootFolderEntity("", "", false)))
    override val folders = _folders.asStateFlow()

    init {
        scope.launch {
            launch {
                settingsRepository.getSettings().collect {
                    _settings.value = it
                }
            }
            launch {
                foldersRepository.getFolders().collect {list ->
                    val temp = list.toMutableList()
                    _folders.value = temp
                }
            }
        }
    }

    override fun onFolderChange(folder: RootFolderEntity?) {
        scopeIO.launch {
            if(folder == null) {
                folders.value.forEach {
                    if(it.isCurrent) {
                        foldersRepository.updateFolder(it.copy(isCurrent = false))
                    }
                }
            } else {
                folders.value.forEach {
                    if(it.isCurrent) {
                        foldersRepository.updateFolder(it.copy(isCurrent = false))
                    }
                }
                foldersRepository.updateFolder(folder.copy(isCurrent = true))
            }
        }
    }

    override fun onFolderButtonClick() {
        onFolderButtonClicked()
    }

    override fun onGridButtonClick() {
        scope.launch {
            var index = settings.value.grid.ordinal
            index = if (index + 1 == cat.petrushkacat.audiobookplayer.domain.models.Grid.values().size) 0 else ++index
            settingsRepository.saveSettings(
                cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity(
                    id = settings.value.id,
                    versionCode = settings.value.versionCode,
                    autoSleepTime = settings.value.autoSleepTime,
                    rewindTime = settings.value.rewindTime,
                    autoRewindBackTime = settings.value.autoRewindBackTime,
                    theme = settings.value.theme,
                    grid = cat.petrushkacat.audiobookplayer.domain.models.Grid.values()[index]
                )
            )
        }
    }

    override fun onSearch(text: String) {
        onSearched(text)
    }

}