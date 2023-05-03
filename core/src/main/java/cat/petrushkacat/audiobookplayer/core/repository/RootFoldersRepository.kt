package cat.petrushkacat.audiobookplayer.core.repository

import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.Flow

interface RootFoldersRepository {

    suspend fun getFolders(): Flow<List<RootFolderEntity>>

    suspend fun addFolder(folder: RootFolderEntity)

    suspend fun deleteFolder(folder: RootFolderEntity)

}