package cat.petrushkacat.audiobookplayer.data.dto

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Keep
@Entity(
    tableName = "BookEntity",
    primaryKeys = ["folderName", "duration"])
data class BookEntityDB(
    val folderUri: String,
    /**
     * Primary key. Do not touch this unless you want to save a primary new book
     */
    val folderName: String,
    /**
     * name that will be displayed to users. Can be changed
     */
    val name: String,
    val chapters: Chapters,
    val currentChapter: Int = 0,
    val currentChapterTime: Long = 0,
    val currentTime: Long = 0,
    /**
     * Primary key. Do not touch this unless you want to save a primary new book
     */
    val duration: Long = 0,
    val rootFolderUri: String,
    val image: ByteArray?,
    val isStarted: Boolean = false,
    val isCompleted: Boolean = false,
    val volumeUp: Float = 0f,
    val playSpeed: Float = 1f,
    val notes: Notes = Notes(emptyList()),
    val lastTimeListened: Long = 0L,
    val isFavorite: Boolean = false,
    val isTemporarilyDeleted: Boolean = false,
    val isWantToListen: Boolean = false,
    @ColumnInfo(defaultValue = "0") val addedTime: Long = 0
)

@Keep
@Serializable
data class Notes(
    val notes: List<Note>
)

@Keep
@Serializable
data class Note(
    val chapterIndex: Int,
    val chapterName: String,
    val time: Long,
    val description: String = ""
)

@Keep
@Serializable
data class Chapters(
    val chapters: List<Chapter>
)
@Keep
@Serializable
data class Chapter(
    val bookFolderUri: String,
    val name: String,
    val duration: Long,
    var timeFromBeginning: Long,
    val uri: String,
)