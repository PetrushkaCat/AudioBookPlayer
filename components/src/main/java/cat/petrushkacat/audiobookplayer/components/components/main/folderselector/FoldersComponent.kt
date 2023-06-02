package cat.petrushkacat.audiobookplayer.components.components.main.folderselector

import android.net.Uri
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import kotlinx.coroutines.flow.StateFlow

interface FoldersComponent {

    val models: StateFlow<List<RootFolderEntity>>

    val foldersToProcess: StateFlow<Int>
    val foldersProcessed: StateFlow<Int>
    fun onFolderSelected(uri: Uri?)
    fun onFolderRemoveButtonClick(rootFolderEntity: RootFolderEntity)

    fun onBack()
}