package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponent
import cat.petrushkacat.audiobookplayer.core.models.Note

@Composable
fun NotesComponentUi(component: NotesComponent) {

    val model = component.models.collectAsState()
    val showDialog = rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(7f)) {
            items(model.value.notes.notes.size) {
                NoteItem(
                    note = model.value.notes.notes[it],
                    component::onNoteClick,
                    component::deleteNote,
                    component::editNote
                )
            }
        }
        Icon(Icons.Default.AddCircle, null, modifier = Modifier
            .clickable {
                showDialog.value = true
            }
            .align(Alignment.End)
            .weight(1f)
            .padding(horizontal = 20.dp)
            .size(40.dp)
        )
    }
    if (showDialog.value) {
        EditDialog(description = "", showDialog = showDialog, onDescriptionChange = {
            component.addNote(it)
        })
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Int, Long) -> Unit,
    onDeleteClick: (Note) -> Unit,
    onDescriptionChange: (Note, String) -> Unit)
{

    val showDialog = rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 7.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNoteClick(note.chapterIndex, note.time)
            }
        ) {
            Column(modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(note.chapterName, style = TextStyle(fontSize = 14.sp))
                Text(formatDuration(note.time), style = TextStyle(fontSize = 14.sp))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = note.description, modifier = Modifier.weight(5f))

            Column(modifier = Modifier.weight(1f).padding(horizontal = 5.dp)) {
                Icon(Icons.Default.Delete, null, modifier = Modifier
                    .clickable {
                        onDeleteClick(note)
                    }
                    .align(Alignment.CenterHorizontally)
                    .size(24.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Icon(Icons.Default.Edit, null, modifier = Modifier
                    .clickable {
                        showDialog.value = true
                    }
                    .align(Alignment.CenterHorizontally)
                    .size(24.dp)
                )
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        )
    }

    if(showDialog.value) {
        EditDialog(description = note.description,
            showDialog = showDialog, onDescriptionChange = {
                onDescriptionChange(
                    Note(
                        note.chapterIndex,
                        note.chapterName,
                        note.time,
                        it),
                    note.description
                )
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    description: String,
    showDialog: MutableState<Boolean>,
    onDescriptionChange: (String) -> Unit)
{

    val newDescription = rememberSaveable { mutableStateOf(description) }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        confirmButton = {
            Icon(
            Icons.Default.Save,
            null,
            modifier = Modifier
                .clickable {
                    onDescriptionChange(newDescription.value)
                    showDialog.value = false
                }
                .size(25.dp)
        ) },
        text = {
            Column {
                TextField(
                    modifier = Modifier,
                    value = newDescription.value,
                    onValueChange = {
                        newDescription.value = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    )
}