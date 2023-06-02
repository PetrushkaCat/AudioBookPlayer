package cat.petrushkacat.audiobookplayer.domain.models

data class RootFolderEntity(
    val uri: String,
    val name: String,
    val isCurrent: Boolean,
)