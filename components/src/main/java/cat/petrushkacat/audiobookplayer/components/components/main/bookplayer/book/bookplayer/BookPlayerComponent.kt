package cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.book.bookplayer

import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import kotlinx.coroutines.flow.StateFlow

interface BookPlayerComponent {

    val models: StateFlow<BookEntity>

    val currentTimings: StateFlow<CurrentTimings>

    val isPlaying: StateFlow<Boolean>

    fun onPlayerEvent(playerEvent: PlayerEvent)

}