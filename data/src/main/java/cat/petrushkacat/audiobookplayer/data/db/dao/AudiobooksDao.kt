package cat.petrushkacat.audiobookplayer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cat.petrushkacat.audiobookplayer.data.dto.BookEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookNotesEntity
import cat.petrushkacat.audiobookplayer.domain.models.BookUri
import kotlinx.coroutines.flow.Flow

@Dao
interface AudiobooksDao {

    @Query("SELECT * FROM BookEntity")
    fun getAllBookListEntities(): Flow<List<BookListEntity>>

    @Query("SELECT * FROM BookEntity WHERE rootFolderUri = :folderUri")
    fun getAllBookListEntitiesInFolder(folderUri: String): Flow<List<BookListEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAfterParse(books: List<BookEntityDB>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveBookAfterParse(book: BookEntityDB)

    @Update(entity = BookEntityDB::class)
    fun updateBook(book: BookNotesEntity)

    @Query("SELECT * FROM BookEntity WHERE folderUri = :bookFolderUri")
    fun getBook(bookFolderUri: String): Flow<BookEntity>

    @Query("DELETE FROM BookEntity WHERE folderUri in (:booksUris)")
    fun deleteByUris(booksUris: List<String>)

    @Query("SELECT * FROM BookEntity")
    fun getUris(): Flow<List<BookUri>>

    @Query("DELETE FROM BookEntity WHERE rootFolderUri = :rootFolderUri")
    fun deleteAllInFolder(rootFolderUri: String)

    @Query("DELETE FROM BookEntity WHERE folderUri = :uri")
    fun deleteBook(uri: String)

    @Query("DELETE FROM BookEntity WHERE folderUri NOT IN (:uris) AND rootFolderUri IN (:rootFolderUris)")
    fun deleteIfNoInList(uris: List<String>, rootFolderUris: List<String>)

    @Update(entity = BookEntityDB::class)
    fun updateBook(book: BookEntityDB)

}