package cat.petrushkacat.audiobookplayer.domain.usecases

import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAudiobooksRepository : AudiobooksRepository {

    val fakeListDB: MutableList<BookListEntity> = createBooks().toMutableList()

    override suspend fun saveAfterParse(books: List<BookEntity>) {
        TODO("Not yet implemented")
    }

    override suspend fun saveBookAfterParse(book: BookEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBookListEntities(): Flow<List<BookListEntity>> = flow {
        emit(fakeListDB)
    }

    override suspend fun getAllBookListEntitiesInFolder(rootFolder: String): Flow<List<BookListEntity>> = flow {
        emit(fakeListDB.filter
        {
            it.rootFolderUri == rootFolder
        })
    }

    override suspend fun updateBook(book: BookNotesEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun updateBook(book: BookEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getBook(bookFolder: String): Flow<BookEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllInFolder(rootFolderUri: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBook(uri: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteIfNoInList(uris: List<String>, rootFolderUris: List<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun getBooksUris(): Flow<List<BookUri>> {
        TODO("Not yet implemented")
    }

    private fun createBooks(): List<BookListEntity> {
        val list = mutableListOf<BookListEntity>()
        repeat(10) {
            list.add(
                BookListEntity(
                    ByteArray(it),
                    it.toString(),
                    it.toString(),
                    it.toLong(),
                    it.toLong(),
                    it % 2 == 0, //0, 2, 4, 6, 8
                    it % 3 == 0,// 0, 3, 6, 9
                    it.toLong(),
                    it % 4 == 0,//0, 4, 8
                    it % 5 == 0,
                    (it % 2 == 0).toString()
                )
            )
        }
        return list
    }
}