package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BookListItem(
    model: BooksListComponent.Model,
    modifier: Modifier
) {

    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.IO).launch {
            bitmap.value = if(model.image != null) {
                BitmapFactory.decodeByteArray(model.image, 0, model.image!!.size)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.round_play_button)
            }
        }
    }

    Column(
        modifier = modifier
            .height(150.dp)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            if(bitmap.value != null) {
                Image(
                    modifier = Modifier
                        .size(100.dp),
                    bitmap = bitmap.value!!.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.book_cover),
                )
            }
            Text(
                model.name,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Column {
            LinearProgressIndicator(
                progress = model.currentTime.toFloat() / model.duration,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatDuration(model.currentTime), style = TextStyle(fontSize = 12.sp))
                Text(formatDuration(model.duration), style = TextStyle(fontSize = 12.sp))
            }
        }
    }
}

@Composable
fun BookGridItem(model: BooksListComponent.Model, modifier: Modifier) {

    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.IO).launch {
            bitmap.value = if(model.image != null) {
                BitmapFactory.decodeByteArray(model.image, 0, model.image!!.size)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.round_play_button)
            }
        }
    }
    Box(modifier = modifier.padding(4.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if(bitmap.value != null) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    bitmap = bitmap.value!!.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.book_cover),
                )
            }
            Column(
                modifier = Modifier.defaultMinSize(minHeight = 72.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(model.name, maxLines = 3, style = TextStyle(fontSize = 14.sp))
                Column {
                    LinearProgressIndicator(progress = model.currentTime.toFloat() / model.duration)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(formatDuration(model.currentTime), style = TextStyle(fontSize = 12.sp))
                        Text(formatDuration(model.duration), style = TextStyle(fontSize = 12.sp))
                    }
                }
            }
        }

    }
}