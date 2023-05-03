package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.runtime.Composable
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookPlayerComponent

@Composable
fun BookPlayerComponentUi(component: BookPlayerComponent) {
    BookComponentUi(component = component.bookComponent)
}