package cat.petrushkacat.audiobookplayer.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["name", "duration"])
data class BookEntity(
    val folderUri: String,
    val name: String,
    val chapters: Chapters,
    val currentChapter: Int = -1,
    val currentChapterTime: Long = -1,
    val currentTime: Long = -1,
    val duration: Long = -1,
    val rootFolderUri: String,
    val imageUri: String?
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