package cat.petrushkacat.audiobookplayer.domain.usecases.folders

import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository

class DeleteFolderUseCase(
    private val rootFoldersRepository: RootFoldersRepository
) {

    suspend operator fun invoke(folder: RootFolderEntity) {
        rootFoldersRepository.deleteFolder(folder)
    }
}