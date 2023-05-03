package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.models.Book
import kotlinx.coroutines.flow.StateFlow

interface BookComponent {

    val models: StateFlow<Model>

    data class Model(
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
}