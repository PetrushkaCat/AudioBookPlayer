package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.book.BookPlayerContainerComponent

@ExperimentalMaterial3Api
@Composable
fun BookPlayerContainerComponentUi(component: BookPlayerContainerComponent) {

    Column {
        BookPlayerToolbarComponentUi(component = component.bookPlayerToolbarComponent)
        BookPlayerComponentUi(component = component.bookPlayerComponent)
    }
}