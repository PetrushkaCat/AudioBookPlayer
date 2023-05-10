package cat.petrushkacat.audiobookplayer.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RootFolderEntity(
    @PrimaryKey val uri: String,
    val name: String,
    val isCurrent: Boolean,
)