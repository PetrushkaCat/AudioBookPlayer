package cat.petrushkacat.audiobookplayer.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["folderName", "duration"])
data class BookEntity(
    val folderUri: String,
    val folderName: String,
    val name: String,
    val chapters: Chapters,
    val currentChapter: Int = 0,
    val currentChapterTime: Long = 0,
    val currentTime: Long = 0,
    val duration: Long = 0,
    val rootFolderUri: String,
    val imageUri: String?,
    val isStarted: Boolean = false,
    val isCompleted: Boolean = false
    )

data class Chapters(
    val chapters: List<Chapter>
)
data class Chapter(
    val bookFolderUri: String,
    val name: String,
    val duration: Long,
    val uri: String,
)

data class BookUri(
    val folderUri: String
)