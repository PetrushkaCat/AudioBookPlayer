package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector

import android.net.Uri
import android.util.Log
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class FolderSelectorComponentImpl(
    componentContext: ComponentContext,
    val onFolderSelect: (Uri) -> Unit
) : FolderSelectorComponent, ComponentContext by componentContext {

    override val models: StateFlow<List<RootFolderEntity>>
        get() = TODO("Not yet implemented")

    override fun onFolderSelected(uri: Uri?) {
        uri?.let {
            Log.d("folder_uri", uri.path!!)
            onFolderSelect(uri)
        }
    }
}