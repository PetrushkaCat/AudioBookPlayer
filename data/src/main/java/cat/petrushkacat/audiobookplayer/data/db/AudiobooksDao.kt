package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponent
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAfterParse(books: List<BookEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveBookAfterParse(book: BookEntity)

    @Update(entity = BookEntity::class)
    fun updateBook(book: BookPlayerComponent.UpdateInfo)

    @Update(entity = BookEntity::class)
    fun updateBook(book: NotesComponent.Model)

    @Query("SELECT * FROM BookEntity WHERE folderUri = :bookFolderUri")
    fun getBook(bookFolderUri: String): Flow<BookEntity>

    @Query("DELETE FROM BookEntity WHERE folderUri in (:booksUris)")
    fun deleteByUris(booksUris: List<String>)

    @Query("SELECT * FROM BookEntity")
    fun getUris(): List<BookUri>

    @Query("DELETE FROM BookEntity WHERE rootFolderUri = :rootFolderUri")
    fun deleteAllInFolder(rootFolderUri: String)

    @Query("DELETE FROM BookEntity WHERE folderUri = :uri")
    fun deleteBook(uri: String)

    @Query("DELETE FROM BookEntity WHERE folderUri NOT IN (:uris)")
    fun deleteIfNoInList(uris: List<String>)

    @Update(entity = BookEntity::class)
    fun updateBook(book: BookEntity)

}