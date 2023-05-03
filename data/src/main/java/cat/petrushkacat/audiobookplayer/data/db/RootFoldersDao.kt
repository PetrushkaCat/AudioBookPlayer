package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Query
import cat.petrushkacat.audiobookplayer.data.model.RootFolderEntity

@Dao
interface RootFoldersDao {
    @Query("SELECT * FROM RootFolderEntity")
    fun getFolders(): List<RootFolderEntity>

}