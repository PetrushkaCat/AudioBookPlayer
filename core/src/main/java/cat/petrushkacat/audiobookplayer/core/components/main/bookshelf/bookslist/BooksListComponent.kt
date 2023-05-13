package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface BooksListComponent {

    val models: StateFlow<List<Model>>

    val settings: StateFlow<SettingsEntity>

    val foldersToProcess: StateFlow<Int>
    val foldersProcessed: StateFlow<Int>
    val isRefreshing: StateFlow<Boolean>

    fun onBookClick(uri: Uri)

    fun refresh()

    data class Model(
        val imageUri: String? = null,
        val name: String,
        val folderUri: String,
        val currentTime: Long = 0,
        val duration: Long = 0,
        val isStarted: Boolean = false,
        val isCompleted: Boolean = false,
        val lastTimeListened: Long = 0L
    )
}