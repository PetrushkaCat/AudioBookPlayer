package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface BookshelfToolbarComponent {

    val settings: StateFlow<SettingsEntity>

    val folders: StateFlow<List<RootFolderEntity>>

    fun onFolderChange(folder: RootFolderEntity?)

    fun onFolderButtonClick()

    fun onGridButtonClick()

    fun onSearch(text: String)
}