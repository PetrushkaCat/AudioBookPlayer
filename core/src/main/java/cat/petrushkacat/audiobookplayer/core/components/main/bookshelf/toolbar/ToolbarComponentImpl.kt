package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector.FolderSelectorComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector.FolderSelectorComponentImpl
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext

class ToolbarComponentImpl(
    componentContext: ComponentContext,
    onFolderSelect: (Uri) -> Unit
) : ToolbarComponent, ComponentContext by componentContext {

    override val folderSelectorComponent = FolderSelectorComponentImpl(
        childContext("folder_selector"),
        onFolderSelect = onFolderSelect
    )

}