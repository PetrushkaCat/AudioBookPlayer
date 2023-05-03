package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RootFoldersDao {
    @Query("SELECT * FROM RootFolderEntity")
    fun getFolders(): Flow<List<RootFolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFolder(folder: RootFolderEntity)

    @Delete
    fun deleteFolder(folder: RootFolderEntity)

}