package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.BookshelfComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfComponentUi(component: BookshelfComponent) {
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
        DrawerComponentUi(component = component.drawerComponent)
    },
        drawerState = drawerState,
    ) {
        Column {
            BookshelfToolbarComponentUi(component = component.bookshelfToolbarComponent, {
                scope.launch {
                    drawerState.open()
                }
            })
            BooksListComponentUi(component = component.booksListComponent)
        }
    }
}