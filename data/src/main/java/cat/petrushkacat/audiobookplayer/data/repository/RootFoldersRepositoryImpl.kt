package cat.petrushkacat.audiobookplayer.data.repository

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector.FolderSelectorComponent
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.data.db.RootFoldersDao

class RootFoldersRepositoryImpl(
    private val rootFoldersDao: RootFoldersDao
): RootFoldersRepository {

    override fun getFolders(): List<FolderSelectorComponent.Model> {
        TODO("Not yet implemented")
    }

    override fun addFolder(folder: FolderSelectorComponent.Model) {
        TODO("Not yet implemented")
    }

    override fun deleteFolder(folderUri: Uri) {
        TODO("Not yet implemented")
    }
}