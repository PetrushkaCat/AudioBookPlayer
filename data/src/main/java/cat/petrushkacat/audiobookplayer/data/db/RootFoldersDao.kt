package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cat.petrushkacat.audiobookplayer.data.dto.RootFolderEntityDB
import kotlinx.coroutines.flow.Flow


@Dao
interface RootFoldersDao {
    @Query("SELECT * FROM RootFolderEntity")
    fun getFolders(): Flow<List<RootFolderEntityDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFolder(folder: RootFolderEntityDB)

    @Delete
    fun deleteFolder(folder: RootFolderEntityDB)

    @Update
    fun updateFolder(folder: RootFolderEntityDB)

}