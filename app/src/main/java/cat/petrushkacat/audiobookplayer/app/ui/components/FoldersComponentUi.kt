package cat.petrushkacat.audiobookplayer.app.ui.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.app.ui.theme.Purple40
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.FoldersComponent
import cat.petrushkacat.audiobookplayer.components.states.RefreshingStates
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity

@Composable
fun FoldersComponentUi(component: FoldersComponent) {

    val context = LocalContext.current
    val model = component.models.collectAsState()
    val foldersToProcess by component.foldersToProcess.collectAsState()
    val foldersProcessed by component.foldersProcessed.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree(),
        onResult = component::onFolderSelected
    )

    val strokeWidth = 5.dp
    val circleColor = Purple40

    Column {
        CommonTopAppBar(
            title = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.folders),
            onBack = {
            component.onBack()
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            if (foldersToProcess != 0 && foldersProcessed < foldersToProcess) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.drawBehind {
                            drawCircle(
                                circleColor,
                                radius = size.width / 2 - strokeWidth.toPx() / 2,
                                style = Stroke(strokeWidth.toPx())
                            )
                        },
                        color = Color.LightGray,
                        strokeWidth = strokeWidth
                    )
                    Row {
                        Text(stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.folders_processed) + " " + foldersProcessed.toString() + " ")
                        Text(stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.of) + " " + foldersToProcess.toString())
                    }
                }
            }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val notAvailableText = stringResource(cat.petrushkacat.audiobookplayer.strings.R.string.not_available_during_scan)

                Text(
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.add_folder_description),
                    modifier = Modifier.weight(5f),
                    style = TextStyle(color = Color.Gray)
                )

                Icon(imageVector = Icons.Default.AddCircle,
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.add_folder_icon),
                    modifier = Modifier
                        .clickable {
                            if (
                                !RefreshingStates.isAutomaticallyRefreshing &&
                                !RefreshingStates.isManuallyRefreshing &&
                                !RefreshingStates.isAddingNewFolder
                            ) {
                                launcher.launch(null)
                            } else {
                                Toast.makeText(
                                    context,
                                    notAvailableText,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .weight(1f)
                        .size(48.dp)
                        .padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun FolderItem(folder: RootFolderEntity, onDelete: (RootFolderEntity) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(folder.name, style = TextStyle(fontSize = 20.sp), modifier = Modifier.weight(9f))
            Icon(Icons.Default.Delete,
                stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.remove_folder_icon),
                modifier = Modifier
                    .clickable {
                        onDelete(folder)
                    }
                    .size(44.dp)
                    .padding(10.dp)
                    .weight(1.5f)
                    .align(Alignment.Bottom)
            )
        }
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), thickness = 1.dp)
    }
}