package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.MarkedBooksListComponentUi
import cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists.completedbooks.CompletedBooksComponent

@Composable
fun CompletedBooksComponentUi(component: CompletedBooksComponent) {

    MarkedBooksListComponentUi(
        component = component,
        title = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.completed_books)
    )

}