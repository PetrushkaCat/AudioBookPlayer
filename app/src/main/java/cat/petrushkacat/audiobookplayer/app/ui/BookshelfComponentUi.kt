package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponent
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