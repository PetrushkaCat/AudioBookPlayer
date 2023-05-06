package cat.petrushkacat.audiobookplayer.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.folderselector.FolderSelectorComponent

@Composable
fun FolderSelectorComponentUi(component: FolderSelectorComponent) {

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree(), onResult =
        component::onFolderSelected
    )

    Icon(imageVector = Icons.Default.Folder, null, modifier = Modifier.clickable{
        launcher.launch(null)
    })
}