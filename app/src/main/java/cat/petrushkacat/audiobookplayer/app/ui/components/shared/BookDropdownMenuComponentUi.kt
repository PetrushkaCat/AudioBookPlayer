package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropdownMenuComponent

@Composable
fun BookDropdownMenuComponentUi(
    component: BookDropdownMenuComponent,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {

    val showEditDialog = rememberSaveable { mutableStateOf(false) }
    val selectedBook by component.selectedBook.collectAsState()

    val favoritesText = if (!selectedBook.isFavorite) stringResource(id = R.string.add_to_favorites)
    else stringResource(id = R.string.remove_from_favorites)

    val listenLaterText =
        if (!selectedBook.isWantToListen) stringResource(id = R.string.add_to_listen_later)
        else stringResource(id = R.string.remove_from_listen_later)

    val completedText = if (!selectedBook.isCompleted) stringResource(id = R.string.mark_as_completed)
    else stringResource(id = R.string.mark_as_not_completed)

    DropdownMenu(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            )
            .border(1.dp, Color.White, RoundedCornerShape(8.dp)),

        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        DropDownItem(
            stringResource(id = R.string.change_name),
            { showEditDialog.value = true },
            showEditDialog,
            {
                component.onBookDropDownEvent(
                    selectedBook.folderUri,
                    BookDropdownMenuComponent.BookDropDownEvent.BookNameChange(it)
                )
                showEditDialog.value = false
                expanded.value = false
            },
            selectedBook.name
        )
        DropDownItem(stringResource(id = R.string.change_cover), {})
        DropDownItem(favoritesText, {
            component.onBookDropDownEvent(
                selectedBook.folderUri,
                BookDropdownMenuComponent.BookDropDownEvent.FavoriteOrNotFavorite
            )
            expanded.value = false
        })
        DropDownItem(listenLaterText, {
            component.onBookDropDownEvent(
                selectedBook.folderUri,
                BookDropdownMenuComponent.BookDropDownEvent.MoveToOrOutListenLater
            )
            expanded.value = false
        })
        DropDownItem(completedText, {
            component.onBookDropDownEvent(
                selectedBook.folderUri,
                BookDropdownMenuComponent.BookDropDownEvent.MarkOrUnmarkAsCompleted
            )
            expanded.value = false
        })

    }
}

@Composable
fun DropDownItem(
    name: String,
    onClick: () -> Unit,
    showDialog: MutableState<Boolean>? = null,
    onEditCompleted: (String) -> Unit = {},
    oldBookName: String? = null

) {

    Column(
        modifier = Modifier
            .height(50.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 16.sp)
        )
    }

    showDialog?.let {
        if (showDialog.value) {
            EditDialog(
                description = oldBookName!!,
                showDialog = showDialog,
                onDescriptionChange = onEditCompleted
            )
        }
    }

}