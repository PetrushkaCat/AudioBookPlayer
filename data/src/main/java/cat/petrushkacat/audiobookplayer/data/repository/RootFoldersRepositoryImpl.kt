package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.data.db.dao.RootFoldersDao
import cat.petrushkacat.audiobookplayer.data.mappers.toRootFolderEntity
import cat.petrushkacat.audiobookplayer.data.mappers.toRootFolderEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository
import kotlinx.coroutines.flow.map

class RootFoldersRepositoryImpl(
    private val rootFoldersDao: RootFoldersDao
): RootFoldersRepository {

    override suspend fun getFolders() = rootFoldersDao.getFolders().map {
        it.map { rootFolderEntityDB ->
            rootFolderEntityDB.toRootFolderEntity()
        }
    }

    override suspend fun addFolder(folder: RootFolderEntity) {
        rootFoldersDao.addFolder(folder.toRootFolderEntityDB())
    }

    override suspend fun deleteFolder(folder: RootFolderEntity) {
        rootFoldersDao.deleteFolder(folder.toRootFolderEntityDB())
    }

    override suspend fun updateFolder(folder: RootFolderEntity) {
        rootFoldersDao.updateFolder(folder.toRootFolderEntityDB())
    }
}