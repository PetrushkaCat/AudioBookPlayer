package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.BookUri
import kotlinx.coroutines.flow.Flow

@Dao
interface AudiobooksDao {

    @Query("SELECT * FROM BookEntity")
    fun getAll(): Flow<List<BooksListComponent.Model>>

    @Query("SELECT * FROM BookEntity WHERE rootFolderUri = :folderUri")
    fun getBooksInFolder(folderUri: String): Flow<List<BooksListComponent.Model>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveAfterParse(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveBookAfterParse(book: BookEntity)

    @Update
    fun updateBook(book: BookEntity)

    @Query("SELECT * FROM BookEntity WHERE folderUri = :bookFolderUri")
    fun getBook(bookFolderUri: String): Flow<BookEntity>

    @Query("DELETE FROM BookEntity WHERE folderUri in (:booksUris)")
    fun deleteByUris(booksUris: List<String>)

    @Query("SELECT * FROM BookEntity")
    fun getUris(): List<BookUri>
}