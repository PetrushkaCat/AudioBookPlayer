package cat.petrushkacat.audiobookplayer.app.ui.components

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.domain.models.Chapters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BookPlayerComponentUi(component: BookPlayerComponent) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val model by component.models.collectAsState()
    val currentTimings by component.currentTimings.collectAsState()
    val progress = try {
        if (currentTimings.currentTimeInChapter > 0) {
            currentTimings.currentTimeInChapter.toFloat() / model.chapters.chapters[currentTimings.currentChapterIndex].duration
        } else {
            0f
        }
    } catch (e: Exception) {
        0f
    }

    val currentTime = try {
        (model.chapters.chapters[currentTimings.currentChapterIndex].timeFromBeginning + currentTimings.currentTimeInChapter)
    } catch (e: Exception) {
        0
    }

    val currentProgress = (currentTime.toFloat() + 1) / model.duration

    val isPlaying by component.isPlaying.collectAsState()

    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = model) {
        scope.launch(Dispatchers.IO) {
            bitmap.value = if (model.image != null) {
                BitmapFactory.decodeByteArray(model.image, 0, model.image!!.size)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.round_play_button)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            LinearProgressIndicator(
                currentProgress, modifier = Modifier
                    .requiredHeight(8.dp)
                    .fillMaxWidth()
                    .padding(2.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatDuration(currentTime), style = TextStyle(fontSize = 13.sp))
                Text(formatDuration(model.duration), style = TextStyle(fontSize = 13.sp))
            }
        }
        Text(model.name, style = TextStyle(fontSize = 17.sp))

        if(bitmap.value != null) {
            Image(
                modifier = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    val width = LocalConfiguration.current.screenHeightDp.dp - 400.dp
                    if (width > LocalConfiguration.current.screenWidthDp.dp) {
                        Modifier.height(LocalConfiguration.current.screenWidthDp.dp)
                    } else Modifier.height(width)
                } else {
                    Modifier.height(LocalConfiguration.current.screenHeightDp.dp - 300.dp)
                },
                bitmap = bitmap.value!!.asImageBitmap(),
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.book_cover),
            )
        }
        PlayerControllers(
            onPlayerEvent = component  ::onPlayerEvent,
            chapters = model.chapters,
            currentTimings = currentTimings,
            progress,
            //model.duration,
            isPlaying,
            Modifier.defaultMinSize(minHeight = 190.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun PlayerControllers(
    onPlayerEvent: (PlayerEvent) -> Unit,
    chapters: Chapters,
    currentTimings: CurrentTimings,
    progress: Float,
    //duration: Long,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {

    var listSize = 0
    val currentChapterName = try {
        listSize = chapters.chapters.size
        chapters.chapters[currentTimings.currentChapterIndex].name
    } catch (e: Exception) {
        listSize = 0
        ""
    }
    val showDialog = rememberSaveable { mutableStateOf(false) }

    val icon = if (!isPlaying) {
        Icons.Default.PlayArrow
    } else {
        Icons.Default.Pause
    }

    Column(
        modifier = modifier.padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (listSize > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.previous_chapter),
                    modifier = Modifier
                        .clickable { onPlayerEvent(PlayerEvent.PreviousChapter) }
                        .size(48.dp)
                        .padding(10.dp)
                        .weight(1f))
                Row(modifier = Modifier
                    .clickable { showDialog.value = true }
                    .weight(4f)
                    .defaultMinSize(minHeight = 48.dp)
                    .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(currentChapterName)
                    Icon(Icons.Default.ExpandMore,
                        stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.expand_chapters),
                        modifier = Modifier
                            .defaultMinSize(30.dp))
                }

                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.next_chapter),
                    modifier = Modifier
                        .clickable { onPlayerEvent(PlayerEvent.NextChapter) }
                        .size(48.dp)
                        .padding(10.dp)
                        .weight(1f)
                )
            }
        }

        PlayerBar(
            progress = progress,
            progressString = formatDuration(currentTimings.currentTimeInChapter),
            duration = try {
                chapters.chapters[currentTimings.currentChapterIndex].duration
            } catch (e: Exception) { 0 },
            onPlayerEvent = onPlayerEvent
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.KeyboardDoubleArrowLeft,
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.long_rewind_back),
                modifier = Modifier
                    .clickable { onPlayerEvent(PlayerEvent.GreatBackward) }
                    .size(48.dp)
                    .padding(4.dp)
                    .weight(1f))
            Icon(
                Icons.Default.KeyboardArrowLeft,
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.rewind_back),
                modifier = Modifier
                    .clickable { onPlayerEvent(PlayerEvent.Backward) }
                    .size(48.dp)
                    .padding(4.dp)
                    .weight(1f))
            Icon(icon,
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.play_pause),
                modifier = Modifier
                    .clickable { onPlayerEvent(PlayerEvent.PlayPause) }
                    .size(48.dp)
                    .padding(4.dp)
                    .weight(2f))
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.rewind_forward),
                modifier = Modifier
                    .clickable { onPlayerEvent(PlayerEvent.Forward) }
                    .size(48.dp)
                    .padding(4.dp)
                    .weight(1f))
            Icon(
                Icons.Default.KeyboardDoubleArrowRight,
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.long_rewind_forward),
                modifier = Modifier
                    .clickable { onPlayerEvent(PlayerEvent.GreatForward) }
                    .size(48.dp)
                    .padding(4.dp)
                    .weight(1f))
        }
    }

    if (showDialog.value) {
        SelectChapterDialog(
            onPlayerEvent,
            chapters = chapters,
            currentTimings = currentTimings,
            showDialog
        )
    }
}

@Composable
fun SelectChapterDialog(
    onPlayerEvent: (PlayerEvent) -> Unit,
    chapters: Chapters,
    currentTimings: CurrentTimings,
    showDialog: MutableState<Boolean>
) {

    Dialog(
        onDismissRequest = { showDialog.value = false },
        content = {
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp),
            ) {
                LazyColumn(
                    state = rememberLazyListState(initialFirstVisibleItemIndex = currentTimings.currentChapterIndex),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(chapters.chapters.size) {
                        Row(
                            modifier = Modifier
                                .background(color = Color.Transparent)
                                .clickable {
                                    onPlayerEvent(PlayerEvent.ChooseChapter(it))
                                    showDialog.value = false
                                }
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (currentTimings.currentChapterIndex == it) {
                                Text(
                                    text = chapters.chapters[it].name,
                                    style = TextStyle(textDecoration = TextDecoration.Underline,
                                        color = MaterialTheme.colorScheme.primary,
                                        //fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                    )
                                )
                            } else {
                                Text(
                                    text = chapters.chapters[it].name, style =
                                        TextStyle(
                                            color = contentColorFor(
                                                backgroundColor = MaterialTheme.colorScheme.background),
                                            //fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PlayerBar(
    progress: Float,
    progressString: String,
    duration: Long,
    onPlayerEvent: (PlayerEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = progress,
            onValueChange = {
                onPlayerEvent(PlayerEvent.UpdateProgress(it))
            },
            modifier = Modifier.padding(4.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(progressString)
            Text(formatDuration(duration))
        }
    }
}