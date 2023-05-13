package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface BookshelfToolbarComponent {

    val settings: StateFlow<SettingsEntity>
    fun onFolderButtonClick()

    fun onGridButtonClick()

    fun onSearch(text: String)
}