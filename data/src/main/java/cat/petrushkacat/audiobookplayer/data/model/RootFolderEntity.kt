package cat.petrushkacat.audiobookplayer.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RootFolderEntity(
    @PrimaryKey val uri: String,
    val name: String,
    val isCurrent: Boolean
)