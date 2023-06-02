package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.UpdateFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.SaveSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookshelfToolbarComponentImpl(
    componentContext: ComponentContext,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val updateFolderUseCase: UpdateFolderUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase,
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
                getSettingsUseCase().collect {
                    _settings.value = it
                }
            }
            launch {
                getFoldersUseCase().collect {list ->
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
                        updateFolderUseCase(it.copy(isCurrent = false))
                    }
                }
            } else {
                folders.value.forEach {
                    if(it.isCurrent) {
                        updateFolderUseCase(it.copy(isCurrent = false))
                    }
                }
                updateFolderUseCase(folder.copy(isCurrent = true))
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
            saveSettingsUseCase(
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