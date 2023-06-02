package cat.petrushkacat.audiobookplayer.domain.models

data class BookListEntity(
    val image: ByteArray? = null,
    val name: String,
    val folderUri: String,
    val currentTime: Long = 0,
    val duration: Long = 0,
    val isStarted: Boolean = false,
    val isCompleted: Boolean = false,
    val lastTimeListened: Long = 0L,
    val isFavorite: Boolean = false,
    val isWantToListen: Boolean = false,
    val rootFolderUri: String = "",
)