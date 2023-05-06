package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.runtime.Composable
import androidx.media3.exoplayer.ExoPlayer
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookPlayerComponent

@Composable
fun BookPlayerComponentUi(component: BookPlayerComponent, player: ExoPlayer) {
    BookComponentUi(component = component.bookComponent, player)
}