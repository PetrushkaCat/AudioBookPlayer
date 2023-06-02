package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository

class DeleteIfNoInListUseCase(
    private val audiobooksRepository: AudiobooksRepository
) {

    /**
     * booksUris - books will not be deleted from db
     * rootFolderUris - books with these root folders can be deleted
     *
     * So, books with the matching rootFolder from the given list and not in booksUris will be deleted from db
     */
    suspend operator fun invoke(booksUris: List<String>, rootFolderUris: List<String>) {
        return audiobooksRepository.deleteIfNoInList(booksUris, rootFolderUris)
    }

}