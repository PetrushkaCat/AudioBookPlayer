package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.EditDialog
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.notes.NotesComponent
import cat.petrushkacat.audiobookplayer.domain.models.Note

@Composable
fun NotesComponentUi(component: NotesComponent) {

    val model = component.models.collectAsState()
    val settings by component.settings.collectAsState()

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val automaticMaxTimeNote = model.value.notes.notes.firstOrNull {
        it.description == stringResource(
            id = cat.petrushkacat.audiobookplayer.strings.R.string.automatic_max_time_note_description)
    }
    val automaticPlayTapNote = model.value.notes.notes.firstOrNull {
        it.description == stringResource(
            id = cat.petrushkacat.audiobookplayer.strings.R.string.automatic_play_tap_note_description)
    }
    Column {
        CommonTopAppBar(title = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.notes)) {
            component.onBack()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    if(automaticMaxTimeNote != null && settings.isMaxTimeAutoNoteEnabled) {
                        NoteItem(note = automaticMaxTimeNote,
                        onNoteClick = component::onNoteClick,
                            onDeleteClick = component::deleteNote,
                            onDescriptionChange = { _, _ -> })
                    }
                }
                item {
                    if(automaticPlayTapNote != null && settings.isOnPlayTapAutoNoteEnabled) {
                        NoteItem(note = automaticPlayTapNote,
                            onNoteClick = component::onNoteClick,
                            onDeleteClick = component::deleteNote,
                            onDescriptionChange = { _, _ -> })
                    }
                }
                items(model.value.notes.notes.size) {
                    if(automaticMaxTimeNote != model.value.notes.notes[it]
                        && automaticPlayTapNote != model.value.notes.notes[it]
                    ) {
                        NoteItem(
                            note = model.value.notes.notes[it],
                            component::onNoteClick,
                            component::deleteNote,
                            component::editNote
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(Icons.Default.AddCircle,
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.add_note_icon),
                    modifier = Modifier
                        .clickable {
                            showDialog.value = true
                        }
                        .size(48.dp)
                        .padding(5.dp)
                )
            }
        }
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

            Column(modifier = Modifier
                .weight(2f)
                .padding(horizontal = 5.dp)) {
                Icon(Icons.Default.Delete,
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.delete_note_icon),
                    modifier = Modifier
                        .clickable {
                            onDeleteClick(note)
                        }
                        .align(Alignment.CenterHorizontally)
                        .size(44.dp)
                        .padding(10.dp)
                )
                Icon(Icons.Default.Edit,
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.edit_note_icon),
                    modifier = Modifier
                        .clickable {
                            showDialog.value = true
                        }
                        .align(Alignment.CenterHorizontally)
                        .size(44.dp)
                        .padding(10.dp)
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
        EditDialog(
            description = note.description,
            showDialog = showDialog,
            onDescriptionChange = {
                onDescriptionChange(
                    note,
                    it
                )
            })
    }
}