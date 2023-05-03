package cat.petrushkacat.audiobookplayer.data.repository

import android.util.Log
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.data.db.RootFoldersDao

class RootFoldersRepositoryImpl(
    private val rootFoldersDao: RootFoldersDao
): RootFoldersRepository {

    override suspend fun getFolders() = rootFoldersDao.getFolders()

    override suspend fun addFolder(folder: RootFolderEntity) {
        Log.d("folderAdd", folder.toString())
        rootFoldersDao.addFolder(folder)
    }

    override suspend fun deleteFolder(folder: RootFolderEntity) {
        rootFoldersDao.deleteFolder(folder)
    }
}