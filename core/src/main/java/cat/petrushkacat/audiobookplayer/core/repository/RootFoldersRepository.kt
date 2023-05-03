package cat.petrushkacat.audiobookplayer.core.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector.FolderSelectorComponent

interface RootFoldersRepository {

    fun getFolders(): List<FolderSelectorComponent.Model>

    fun addFolder(folder: FolderSelectorComponent.Model)

    fun deleteFolder(folderUri: Uri)

}