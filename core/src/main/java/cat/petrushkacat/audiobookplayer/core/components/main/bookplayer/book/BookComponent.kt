package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.net.Uri
import androidx.room.Entity
import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import kotlinx.coroutines.flow.StateFlow

interface BookComponent {

    val models: StateFlow<BookEntity>

    fun onPlayerEvent(playerEvent: PlayerEvent)

    val currentTimings: StateFlow<CurrentTimings>

    data class Model(
        val isPaused: Boolean
    )

    data class UpdateInfo(
        val folderName: String,
        val name: String,
        val currentChapter: Int,
        val currentChapterTime: Long,
        val currentTime: Long,
        val duration: Long,
        val isStarted: Boolean = false,
        val isCompleted: Boolean = false
        //val imageUri: String?
    )

}