package cat.petrushkacat.audiobookplayer.core.models

import android.net.Uri

data class Book(
    val folderUri: Uri,
    val name: String,
    val chapters: List<Chapter>,
    val currentTime: Long = -1,
    val duration: Long = -1,
    val rootFolderUri: Uri,
    val imageUri: Uri?
)

data class Chapter(
    val name: String,
    val uri: Uri,
    val isCurrent: Boolean = false,
    val duration: Long = -1,
)