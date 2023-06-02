package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository

class DeleteAllBooksInFolderUseCase (
    private val audiobooksRepository: AudiobooksRepository
) {

    suspend operator fun invoke(rootFolderUri: String) {
        return audiobooksRepository.deleteAllInFolder(rootFolderUri)
    }
}