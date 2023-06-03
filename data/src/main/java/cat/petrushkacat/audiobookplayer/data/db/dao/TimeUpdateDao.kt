package cat.petrushkacat.audiobookplayer.data.db.dao

import androidx.room.Dao
import androidx.room.Update
import cat.petrushkacat.audiobookplayer.audioservice.UpdateTime
import cat.petrushkacat.audiobookplayer.data.dto.BookEntityDB

@Dao
interface TimeUpdateDao {

    @Update(entity = BookEntityDB::class)
    fun updateTime(updateTime: UpdateTime)
}