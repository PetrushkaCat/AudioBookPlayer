package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface FolderSelectorComponent {

    val models: StateFlow<List<Model>>

    fun onFolderSelected(uri: Uri?)

    data class Model(
        val folderUri: Uri,
        val folderName: String,
        val isCurrent: Boolean
    )
}