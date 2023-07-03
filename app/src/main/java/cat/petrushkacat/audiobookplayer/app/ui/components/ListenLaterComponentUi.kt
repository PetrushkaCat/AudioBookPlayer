package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.MarkedBooksListComponentUi
import cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists.listenlater.ListenLaterComponent
import cat.petrushkacat.audiobookplayer.strings.R

@Composable
fun ListenLaterComponentUi(component: ListenLaterComponent) {

    MarkedBooksListComponentUi(
        component = component,
        title = stringResource(id = R.string.listen_later)
    )

}