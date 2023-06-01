package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.StateFlow

interface BookshelfToolbarComponent {

    val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>

    val folders: StateFlow<List<RootFolderEntity>>

    fun onFolderChange(folder: RootFolderEntity?)

    fun onFolderButtonClick()

    fun onGridButtonClick()

    fun onSearch(text: String)
}