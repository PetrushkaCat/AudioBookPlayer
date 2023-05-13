package cat.petrushkacat.audiobookplayer.core.models

import androidx.room.Entity

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
    )

data class Notes(
    val notes: List<Note>
)

data class Note(
    val chapterIndex: Int,
    val chapterName: String,
    val time: Long,
    val description: String = ""
)

data class Chapters(
    val chapters: List<Chapter>
)
data class Chapter(
    val bookFolderUri: String,
    val name: String,
    val duration: Long,
    var timeFromBeginning: Long,
    val uri: String,
)

data class BookUri(
    val folderUri: String
)