package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Query
import cat.petrushkacat.audiobookplayer.data.model.BookEntity

@Dao
interface AudiobooksDao {

    @Query("SELECT * FROM BookEntity")
    fun getAll(): List<BookEntity>

}