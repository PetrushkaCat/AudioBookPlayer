package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import kotlinx.coroutines.flow.StateFlow

interface BookComponent {

    val models: StateFlow<BookEntity>

    fun onButtonPrevChapterClick()

    fun onButtonNextChapterClick()

    fun onButtonPlusTimeClick()

    fun onButtonMinusButtonClick()

    fun onButtonPlayPauseClick()

    fun onChapterSelectedManually(chapterNumber: Int)

    data class Model(
        val isPaused: Boolean
    )

}