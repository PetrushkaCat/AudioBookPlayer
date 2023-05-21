package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.media3.common.util.UnstableApi
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookPlayerContainerComponent

@ExperimentalMaterial3Api
@UnstableApi
@Composable
fun BookPlayerContainerComponentUi(component: BookPlayerContainerComponent) {

    Column {
        BookPlayerToolbarComponentUi(component = component.bookPlayerToolbarComponent)
        BookPlayerComponentUi(component = component.bookPlayerComponent)
    }
}