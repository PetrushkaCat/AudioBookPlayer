package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface BooksListComponent {

    val models: StateFlow<List<Model>>

    fun onBookClick(uri: Uri)

    data class Model(
        val imageUri: String? = null,
        val name: String,
        val folderUri: String,
        val currentTime: Long = -1,
        val duration: Long = -1,
    )
}