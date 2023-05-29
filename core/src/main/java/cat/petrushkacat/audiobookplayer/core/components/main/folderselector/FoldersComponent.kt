package cat.petrushkacat.audiobookplayer.core.components.main.folderselector

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.StateFlow

interface FoldersComponent {

    val models: StateFlow<List<RootFolderEntity>>

    val foldersToProcess: StateFlow<Int>
    val foldersProcessed: StateFlow<Int>
    fun onFolderSelected(uri: Uri?)
    fun onFolderRemoveButtonClick(rootFolderEntity: RootFolderEntity)

    fun onBack()
}