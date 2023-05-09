package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Update
import cat.petrushkacat.audiobookplayer.audioservice.UpdateTime
import cat.petrushkacat.audiobookplayer.core.models.BookEntity

@Dao
interface TimeUpdateDao {

    @Update(entity = BookEntity::class)
    fun updateTime(updateTime: UpdateTime)
}