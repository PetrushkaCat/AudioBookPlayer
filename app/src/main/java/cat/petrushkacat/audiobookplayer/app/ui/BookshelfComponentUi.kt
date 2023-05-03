package cat.petrushkacat.audiobookplayer.app.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun BookshelfComponentUi(component: BookshelfComponent) {
    val folders = component.folder.collectAsState()

    Column {
        ToolbarComponentUi(component = component.toolbarComponent)
        BooksListComponentUi(component = component.booksListComponent)
        Text("++++++++++")
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(count = folders.value.size) {
                Text(folders.value[it].name)
            }
        }
    }
}