package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer

import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import kotlinx.coroutines.flow.StateFlow

interface BookPlayerComponent {

    val models: StateFlow<BookEntity>

    val currentTimings: StateFlow<CurrentTimings>

    fun onPlayerEvent(playerEvent: PlayerEvent)

    val isPlaying: StateFlow<Boolean>


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
        //val image: ByteArray?
    )

}