package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.isAudio
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    audiobooksRepository: AudiobooksRepository,
    private val bookUri: Uri
) : BookComponent, ComponentContext by componentContext {

    private val scope = componentContext.componentCoroutineScopeDefault()

    override val models: MutableStateFlow<BookEntity> = MutableStateFlow(BookEntity(bookUri.toString(), "null", Chapters(emptyList()), imageUri = null, rootFolderUri = bookUri.toString()))

    init {
        scope.launch {
            audiobooksRepository.getBook(bookUri).collect {
                models.value = it
            }
        }
    }
}