package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarComponentUi(component: ToolbarComponent) {
    TopAppBar(title = {Text("Book Reader")}, actions = {
        FolderSelectorComponentUi(component = component.folderSelectorComponent)
    })
}