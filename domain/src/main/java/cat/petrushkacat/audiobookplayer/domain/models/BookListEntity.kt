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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BookListEntity

        if (image != null) {
            if (other.image == null) return false
            if (!image.contentEquals(other.image)) return false
        } else if (other.image != null) return false
        if (name != other.name) return false
        if (folderUri != other.folderUri) return false
        if (currentTime != other.currentTime) return false
        if (duration != other.duration) return false
        if (isStarted != other.isStarted) return false
        if (isCompleted != other.isCompleted) return false
        if (lastTimeListened != other.lastTimeListened) return false
        if (isFavorite != other.isFavorite) return false
        if (isWantToListen != other.isWantToListen) return false
        if (rootFolderUri != other.rootFolderUri) return false

        return true
    }

    override fun hashCode(): Int {
        var result = image?.contentHashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + folderUri.hashCode()
        result = 31 * result + currentTime.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + isStarted.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + lastTimeListened.hashCode()
        result = 31 * result + isFavorite.hashCode()
        result = 31 * result + isWantToListen.hashCode()
        result = 31 * result + rootFolderUri.hashCode()
        return result
    }
}