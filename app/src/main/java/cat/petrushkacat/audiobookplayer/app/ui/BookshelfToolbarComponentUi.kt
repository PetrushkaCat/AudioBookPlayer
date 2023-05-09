package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.BookshelfToolbarComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfToolbarComponentUi(
    component: BookshelfToolbarComponent,
    onDrawerButtonClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            Icon(Icons.Default.Menu,
            null,
            modifier = Modifier.clickable {
                onDrawerButtonClick()
            }
        )},
        title = {Text("Book Reader")},
        actions = {
        Icon(
            Icons.Default.Folder,
            contentDescription = null,
            modifier = Modifier.clickable {
                component.onFolderButtonClick()
            }
                .padding(horizontal = 10.dp)
        )
    },
        )
}
