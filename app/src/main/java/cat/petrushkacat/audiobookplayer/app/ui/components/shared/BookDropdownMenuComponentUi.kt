package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.components.util.arrayFromBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BookDropdownMenuComponentUi(
    component: BookDropdownMenuComponent,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val showEditDialog = rememberSaveable { mutableStateOf(false) }
    val selectedBook by component.selectedBook.collectAsState()
    //val isTakingPicture = remember { mutableStateOf(false) }

    val favoritesText = if (!selectedBook.isFavorite) stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.add_to_favorites)
    else stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.remove_from_favorites)

    val listenLaterText =
        if (!selectedBook.isWantToListen) stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.add_to_listen_later)
        else stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.remove_from_listen_later)

    val completedText = if (!selectedBook.isCompleted) stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.mark_as_completed)
    else stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.mark_as_not_completed)

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(), onResult = {
        CoroutineScope(Dispatchers.IO).launch {
            expanded.value = false
            it?.let {
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                if (bitmap != null) {
                    component.onBookDropDownEvent(
                        selectedBook.folderUri,
                        BookDropdownMenuComponent.BookDropDownEvent.BookCoverChange(arrayFromBitmap(bitmap))
                    )
                }
            }
        }
    })

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
            stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.change_name),
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
        DropDownItem(stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.change_cover), {
            launcher.launch("image/*")
        })
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