package cat.petrushkacat.audiobookplayer.app.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun BooksListComponentUi(component: BooksListComponent) {

    val model by component.models.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(4.dp),
        state = rememberLazyGridState(0)
    ) {
        items(model.size) {
            BookItem(model = model[it], Modifier.clickable {
                component.onBookClick(Uri.parse(model[it].folderUri))
            })
        }
    }
}


@Composable
fun BookItem(model: BooksListComponent.Model, modifier: Modifier) {

    Box(modifier = modifier.padding(4.dp)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
               modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.imageUri)
                    .error(R.drawable.round_play_button)
                    .build(),
                contentDescription = null,
            )
            Text(model.name, maxLines = 3, style = TextStyle(fontSize = 14.sp))
            LinearProgressIndicator(progress = model.currentTime.toFloat() / model.duration)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatDuration(model.currentTime), style = TextStyle(fontSize = 12.sp))
                Text(formatDuration(model.duration), style = TextStyle(fontSize = 12.sp))
            }
        }
        
    }
}