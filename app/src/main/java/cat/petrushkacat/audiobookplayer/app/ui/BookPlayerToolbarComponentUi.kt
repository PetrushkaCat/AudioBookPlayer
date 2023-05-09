package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponent

@Composable
@ExperimentalMaterial3Api
fun BookPlayerToolbarComponentUi(component: BookPlayerToolbarComponent) {

    val showDialog = rememberSaveable { mutableStateOf(false) }

    TopAppBar(title = { Text("") }, actions = {
        Icon(
            Icons.Default.Speed,
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    showDialog.value = true
                }
                .padding(horizontal = 10.dp)
        )
        Icon(
            Icons.Default.Bookmarks,
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    component.onNotesButtonClick()
                }
                .padding(horizontal = 10.dp)
        )
    })

    if(showDialog.value) {
        PlaySpeedChangeDialog(currentSpeed = component.getPlaySpeed(), showDialog, component::onPlaySpeedChange)
    }
}

@Composable
fun PlaySpeedChangeDialog(
    currentSpeed: Float,
    showDialog: MutableState<Boolean>,
    onPlaySpeedChange: (Float) -> Unit
) {
    val useNewValue = rememberSaveable { mutableStateOf(false) }
    val newValue = rememberSaveable { mutableStateOf(currentSpeed) }

    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
                           },
        confirmButton = {
            Text("Ok", modifier = Modifier.clickable {
                onPlaySpeedChange(newValue.value)
                showDialog.value = false
            })
        },
        text = {
            Column {
                Slider(
                    value = newValue.value,
                    onValueChange = {
                        useNewValue.value = true
                        newValue.value = it
                    },
                    valueRange = 0.25f.rangeTo(3f),
                    steps = 10,
                    onValueChangeFinished = {
                        //useNewValue.value = false
                    }
                )
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("x " + String.format("%.2f", newValue.value))
                }
            }
        }
    )
}