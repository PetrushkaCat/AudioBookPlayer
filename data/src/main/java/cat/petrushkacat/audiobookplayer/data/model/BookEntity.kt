package cat.petrushkacat.audiobookplayer.data.model

import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity(
    @PrimaryKey val folderUri: String,
    val name: String,
    //val chapters: List<Chapter>,
    val currentTime: Long,
    val duration: Long,
    val rootFolderUri: String,
    val imageUri: String?
)

data class Chapter(
    val bookFolderUri: String,
    val name: String,
    val uri: String,
)