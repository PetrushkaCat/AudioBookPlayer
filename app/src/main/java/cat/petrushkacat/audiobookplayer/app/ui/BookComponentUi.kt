package cat.petrushkacat.audiobookplayer.app.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ArrowDropUp
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.audioservice.CurrentTimings
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
@UnstableApi
fun BookComponentUi(component: BookComponent, player: ExoPlayer) {

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


    Log.d("player-2.9", currentTimings.toString())
    Log.d("player-2.91", progress.toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(model.name, style = TextStyle(fontSize = 20.sp))
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
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
            model.duration
        )

    }
}

// TODO add content description
@Composable
fun PlayerControllers(
    onPlayerEvent: (PlayerEvent) -> Unit,
    chapters: Chapters,
    currentTimings: CurrentTimings,
    progress: Float,
    duration: Long
) {

    val iconsSize = 50.dp
    val iconsSize2 = 30.dp
    val isPlaying = rememberSaveable { mutableStateOf(false) }
    val currentChapterName = try {
        chapters.chapters[currentTimings.currentChapterIndex].name
    } catch (e: IndexOutOfBoundsException) {
        ""
    }
    val showDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val icon = if (!isPlaying.value) {
        Icons.Default.PlayArrow
    } else {
        Icons.Default.Pause
    }

    Column(
        modifier = Modifier
            .padding(10.dp),
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
                        .clickable {
                            onPlayerEvent(PlayerEvent.PreviousChapter)
                        })
                Row(modifier = Modifier.clickable {
                    showDialog.value = true
                }) {
                    Text(currentChapterName, modifier = Modifier.align(Alignment.CenterVertically))
                    Icon(Icons.Default.ExpandMore, null, modifier = Modifier.size(iconsSize2))
                }

                Icon(
                    Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier
                        .size(iconsSize2)
                        .clickable {
                            onPlayerEvent(PlayerEvent.NextChapter)
                        }
                )
            }
        }
        Spacer(modifier = Modifier.size(20.dp))

        PlayerBar(
            progress = progress,
            progressString = formatDuration(currentTimings.currentTimeInChapter),
            durationString = try {
                formatDuration(chapters.chapters[currentTimings.currentChapterIndex].duration)
            } catch (e: IndexOutOfBoundsException) { "" },
            onPlayerEvent = onPlayerEvent
        )

        Spacer(modifier = Modifier.size(20.dp))

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
                    isPlaying.value = !isPlaying.value
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
    durationString: String,
    onPlayerEvent: (PlayerEvent) -> Unit
) {

    val newValue = remember { mutableStateOf(0f) }
    val useNewValue = remember { mutableStateOf(false) }

    Log.d("player-3", progress.toString())
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = if (useNewValue.value) newValue.value else progress,
            onValueChange = {
                useNewValue.value = true
                newValue.value = it
                onPlayerEvent(PlayerEvent.UpdateProgress(it))
            },
            modifier = Modifier.padding(4.dp),
            onValueChangeFinished = {
                //useNewValue.value = false
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(progressString)
            Text(durationString)
        }
    }
}