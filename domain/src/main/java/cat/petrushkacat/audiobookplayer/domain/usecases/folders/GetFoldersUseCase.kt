package cat.petrushkacat.audiobookplayer.domain.usecases.folders

import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository
import kotlinx.coroutines.flow.Flow

class GetFoldersUseCase(
    private val rootFoldersRepository: RootFoldersRepository
) {

    suspend operator fun invoke(): Flow<List<RootFolderEntity>> {
        return rootFoldersRepository.getFolders()
    }
}