package cat.petrushkacat.audiobookplayer.domain.repository

import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import kotlinx.coroutines.flow.Flow

interface RootFoldersRepository {

    suspend fun getFolders(): Flow<List<RootFolderEntity>>

    suspend fun addFolder(folder: RootFolderEntity)

    suspend fun deleteFolder(folder: RootFolderEntity)

    suspend fun updateFolder(folder: RootFolderEntity)

}