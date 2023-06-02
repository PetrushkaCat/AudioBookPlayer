package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import kotlinx.coroutines.flow.StateFlow

interface BookshelfToolbarComponent {

    val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>

    val folders: StateFlow<List<RootFolderEntity>>

    fun onFolderChange(folder: RootFolderEntity?)

    fun onFolderButtonClick()

    fun onGridButtonClick()

    fun onSearch(text: String)
}