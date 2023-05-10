package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Grid3x3
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.GridGoldenratio
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.BookshelfToolbarComponent
import cat.petrushkacat.audiobookplayer.core.models.Grid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfToolbarComponentUi(
    component: BookshelfToolbarComponent,
    onDrawerButtonClick: () -> Unit
) {
    val settings = component.settings.collectAsState()

    val icon = when (settings.value.grid) {
        Grid.LIST -> Icons.Default.GridView
        Grid.BIG_CELLS -> Icons.Default.GridOn
        Grid.SMALL_CELLS -> Icons.Default.ListAlt
    }
    TopAppBar(
        navigationIcon = {
            Icon(Icons.Default.Menu,
                null,
                modifier = Modifier
                    .clickable {
                        onDrawerButtonClick()
                    }
                    .padding(horizontal = 10.dp)
            )
        },
        title = { Text("Book Reader") },
        actions = {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        component.onGridButtonClick()
                    }
                    .padding(horizontal = 10.dp)
            )

            Icon(
                Icons.Default.Folder,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        component.onFolderButtonClick()
                    }
                    .padding(horizontal = 10.dp)
            )
        },
    )
}
