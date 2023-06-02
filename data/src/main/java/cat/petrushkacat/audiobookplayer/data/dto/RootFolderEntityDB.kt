package cat.petrushkacat.audiobookplayer.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RootFolderEntity")
data class RootFolderEntityDB(
    @PrimaryKey val uri: String,
    val name: String,
    val isCurrent: Boolean,
)