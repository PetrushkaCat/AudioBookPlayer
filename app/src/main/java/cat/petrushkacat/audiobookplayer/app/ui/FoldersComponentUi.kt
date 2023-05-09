package cat.petrushkacat.audiobookplayer.app.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.core.components.main.folderselector.FoldersComponent
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity

private val iconsSize = 25.dp
@Composable
fun FoldersComponentUi(component: FoldersComponent) {

    val model = component.models.collectAsState()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree(), onResult =
        component::onFolderSelected
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp)) {
        LazyColumn(
            modifier = Modifier.weight(7f),
            state = rememberLazyListState()
        ) {
            items(model.value.size) { index ->
                FolderItem(folder = model.value[index]) {
                    component.onFolderRemoveButtonClick(it)
                }
            }
        }
        Icon(imageVector = Icons.Default.AddCircle, null, modifier = Modifier
            .clickable {
                launcher.launch(null)
            }
            .align(Alignment.End)
            .weight(1f)
            .padding(horizontal = 20.dp)
            .size(40.dp)
        )
    }
}

@Composable
fun FolderItem(folder: RootFolderEntity, onDelete: (RootFolderEntity) -> Unit) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(folder.name, style = TextStyle(fontSize = 20.sp), modifier = Modifier.weight(9f))
            Icon(Icons.Default.Delete, null, modifier = Modifier
                .clickable {
                    onDelete(folder)
                }
                .size(iconsSize)
                .weight(1f)
                .align(Alignment.Bottom)
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), thickness = 1.dp)
    }
}