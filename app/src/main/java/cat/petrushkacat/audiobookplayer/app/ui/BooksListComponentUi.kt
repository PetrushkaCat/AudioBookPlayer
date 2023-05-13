package cat.petrushkacat.audiobookplayer.app.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.ui.theme.Purple40
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent
import cat.petrushkacat.audiobookplayer.core.models.Grid
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BooksListComponentUi(component: BooksListComponent) {

    val model by component.models.collectAsState()
    val settings by component.settings.collectAsState()
    val isRefreshing by component.isRefreshing.collectAsState()
    val foldersToProcess by component.foldersToProcess.collectAsState()
    val foldersProcessed by component.foldersProcessed.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        component.refresh()
    })

    val strokeWidth = 5.dp
    val circleColor = Purple40

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        if(!isRefreshing) {
            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState,
                Modifier
                    .align(Alignment.TopCenter)
                    .clickable { }
            )
        }
        Column {
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
                        Text(stringResource(id = R.string.folders_processed) + " " + foldersProcessed.toString() + " ")
                        Text(stringResource(id = R.string.of) + " " + foldersToProcess.toString())
                    }
                }
            }
            if (settings.grid == Grid.SMALL_CELLS || settings.grid == Grid.BIG_CELLS) {
                val size = if (settings.grid == Grid.BIG_CELLS) 150.dp else 100.dp
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(size),
                    contentPadding = PaddingValues(4.dp),
                    state = rememberLazyGridState(0)
                ) {
                    items(model.size) {
                        BookGridItem(model = model[it], Modifier.clickable {
                            component.onBookClick(Uri.parse(model[it].folderUri))
                        })
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    state = rememberLazyListState()
                ) {
                    items(model.size) {
                        BookListItem(model = model[it], Modifier.clickable {
                            component.onBookClick(Uri.parse(model[it].folderUri))
                        })
                    }
                }
            }
        }
    }
}


@Composable
fun BookListItem(model: BooksListComponent.Model, modifier: Modifier) {
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
            AsyncImage(
                modifier = Modifier
                    .size(100.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.image)
                    .error(R.drawable.round_play_button)
                    .build(),
                contentDescription = stringResource(id = R.string.book_cover),
            )
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

    Box(modifier = modifier.padding(4.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.image)
                    .error(R.drawable.round_play_button)
                    .build(),
                contentDescription = stringResource(id = R.string.book_cover),
            )
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