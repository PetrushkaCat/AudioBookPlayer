package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponent

@Composable
fun BookshelfComponentUi(component: BookshelfComponent) {
    Column {
        ToolbarComponentUi(component = component.toolbarComponent)
        BooksListComponentUi(component = component.booksListComponent)
    }
}