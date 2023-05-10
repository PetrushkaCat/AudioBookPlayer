package cat.petrushkacat.audiobookplayer.app.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
@UnstableApi
fun BookPlayerComponentUi(component: BookPlayerComponent) {

    val context = LocalContext.current
    val model by component.models.collectAsState()
    val currentTimings by component.currentTimings.collectAsState()
    val progress = try {
        if (currentTimings.currentTimeInChapter > 0) {
            currentTimings.currentTimeInChapter.toFloat() / model.chapters.chapters[currentTimings.currentChapterIndex].duration
        } else {
            0f
        }
    } catch (e: IndexOutOfBoundsException) {
        0f
    }

    val currentTime = try {
        (model.chapters.chapters[currentTimings.currentChapterIndex].timeFromBeginning + currentTimings.currentTimeInChapter)
    } catch (e: Exception) {
        0
    }

    val currentProgress = (currentTime.toFloat() + 1) / model.duration

    val isPlaying by component.isPlaying.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            LinearProgressIndicator(
                currentProgress, modifier = Modifier
                    //.weight(0.1f)
                    .requiredHeight(8.dp)
                    .fillMaxWidth()
                    .padding(2.dp)
            )
            Row(
                modifier = Modifier
                    //.weight(0.3f)
                    .fillMaxWidth()
                    .padding(4.dp, 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatDuration(currentTime), style = TextStyle(fontSize = 13.sp))
                Text(formatDuration(model.duration), style = TextStyle(fontSize = 13.sp))
            }
        }
        Text(model.name, style = TextStyle(fontSize = 17.sp), //modifier = Modifier.weight(0.7f)
        )

        AsyncImage(
            modifier = if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT ) {
                val width = LocalConfiguration.current.screenHeightDp.dp - 400.dp
                if(width > LocalConfiguration.current.screenWidthDp.dp) {
                    Modifier.height(LocalConfiguration.current.screenWidthDp.dp)
                } else Modifier.height(width)
            } else {
                Modifier.height(LocalConfiguration.current.screenHeightDp.dp - 300.dp)

                //Modifier.weight(0.3f)
            },
            model = ImageRequest.Builder(LocalContext.current)
                .data(model.imageUri)
                .error(R.drawable.round_play_button)
                .build(),
            contentDescription = null,
        )
        PlayerControllers(
            onPlayerEvent = component::onPlayerEvent,
            chapters = model.chapters,
            currentTimings = currentTimings,
            progress,
            model.duration,
            isPlaying,
            Modifier.defaultMinSize(minHeight = 170.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

    }
}

// TODO add content description for all screens
@Composable
fun PlayerControllers(
    onPlayerEvent: (PlayerEvent) -> Unit,
    chapters: Chapters,
    currentTimings: CurrentTimings,
    progress: Float,
    duration: Long,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {

    val iconsSize = 40.dp
    val iconsSize2 = 25.dp
    //val isPlaying = rememberSaveable { mutableStateOf(false) }
    val currentChapterName = try {
        chapters.chapters[currentTimings.currentChapterIndex].name
    } catch (e: IndexOutOfBoundsException) {
        ""
    }
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val icon = if (!isPlaying) {
        Icons.Default.PlayArrow
    } else {
        Icons.Default.Pause
    }

    Column(
        modifier = modifier
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (chapters.chapters.size > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    null,
                    modifier = Modifier
                        .size(iconsSize2)
                        .weight(1f)
                        .clickable {
                            onPlayerEvent(PlayerEvent.PreviousChapter)
                        })
                Row(modifier = Modifier
                    .weight(9f)
                    .clickable {
                        showDialog.value = true
                    },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(currentChapterName)
                    Icon(Icons.Default.ExpandMore, null, modifier = Modifier
                        .size(iconsSize2)
                        .defaultMinSize(30.dp))
                }

                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconsSize2)
                        .weight(1f)
                        .clickable {
                            onPlayerEvent(PlayerEvent.NextChapter)
                        }
                )
            }
        }

        PlayerBar(
            progress = progress,
            progressString = formatDuration(currentTimings.currentTimeInChapter),
            duration = try {
                chapters.chapters[currentTimings.currentChapterIndex].duration
            } catch (e: IndexOutOfBoundsException) { 0 },
            onPlayerEvent = onPlayerEvent
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.KeyboardDoubleArrowLeft,
                contentDescription = null,
                modifier = Modifier
                    .size(iconsSize)
                    .clickable {
                        onPlayerEvent(PlayerEvent.Backward(15000))
                    })
            Icon(icon, contentDescription = null, modifier = Modifier
                .size(iconsSize)
                .clickable {
                    onPlayerEvent(PlayerEvent.PlayPause)
                    //isPlaying.value = !isPlaying.value
                })
            Icon(
                Icons.Default.KeyboardDoubleArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .size(iconsSize)
                    .clickable {
                        onPlayerEvent(PlayerEvent.Forward(15000))
                    }
            )
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

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        confirmButton = {},
        text = {
            LazyColumn(
                modifier = Modifier.padding(4.dp),
                state = rememberLazyListState(initialFirstVisibleItemIndex = currentTimings.currentChapterIndex)
            ) {
                items(chapters.chapters.size) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(4.dp)
                            .clickable {
                                onPlayerEvent(PlayerEvent.ChooseChapter(it))
                                showDialog.value = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = chapters.chapters[it].name)
                        if (currentTimings.currentChapterIndex == it) {
                            Text(" <--current:)")
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

    Log.d("player-3", progress.toString())
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = progress,
            onValueChange = {
                onPlayerEvent(PlayerEvent.UpdateProgress(it))
            },
            modifier = Modifier.padding(4.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(progressString)
            Text(formatDuration(duration))
        }
    }
}