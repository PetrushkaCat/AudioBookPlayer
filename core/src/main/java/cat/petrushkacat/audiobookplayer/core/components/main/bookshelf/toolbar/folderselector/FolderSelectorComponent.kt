package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.StateFlow

interface FolderSelectorComponent {

    val models: StateFlow<List<RootFolderEntity>>

    fun onFolderSelected(uri: Uri?)
}