package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R

@Composable
fun EditDialog(
    description: String,
    showDialog: MutableState<Boolean>,
    onDescriptionChange: (String) -> Unit)
{

    val newDescription = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(description, selection = TextRange(description.length)))
    }

    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        confirmButton = {
            Icon(
                Icons.Default.Save,
                stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable {
                        onDescriptionChange(newDescription.value.text)
                        showDialog.value = false
                    }
                    .size(48.dp)
                    .padding(10.dp)
            ) },
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onGloballyPositioned {
                            focusRequester.requestFocus()
                        },
                    value = newDescription.value,
                    onValueChange = {
                        newDescription.value = it
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text(stringResource(id = R.string.description)) }
                )
            }
        }
    )
}