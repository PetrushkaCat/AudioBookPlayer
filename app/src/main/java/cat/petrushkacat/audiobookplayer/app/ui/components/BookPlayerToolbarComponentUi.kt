package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponent
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
@ExperimentalMaterial3Api
fun BookPlayerToolbarComponentUi(component: BookPlayerToolbarComponent) {

    val showPlaySpeedDialog = rememberSaveable { mutableStateOf(false) }
    val manualRewindState by component.manualSleepTimerState.collectAsState()
    val showManualSleepTimerConfigurationDialog = rememberSaveable {
        mutableStateOf(false)
    }
    val settings by component.settings.collectAsState()

    TopAppBar(
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    stringResource(id = R.string.back),
                    modifier = Modifier
                        .clickable {
                            component.onBack()
                        }
                        .size(48.dp)
                        .padding(horizontal = 8.dp)
                )
            }
        },
        title = { Text("") },
        actions = {
            Box {
                Column(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = {
                                component.onManualSleep(settings.sleepTimerType, true)
                            },
                            onLongClick = {
                                showManualSleepTimerConfigurationDialog.value = true
                            }
                        )
                        .size(54.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Outlined.Timer,
                        contentDescription = stringResource(id = R.string.speed_icon_description),
                        modifier = Modifier
                            .size(48.dp)
                            .padding(10.dp),
                    )
                }
                if (manualRewindState.isActive) {
                    Text(
                        formatDuration(manualRewindState.sleepAfter),
                        modifier = Modifier.align(Alignment.BottomCenter),
                        style = TextStyle(fontSize = 13.sp)
                    )
                }
            }
            Icon(
                Icons.Default.Speed,
                contentDescription = stringResource(id = R.string.speed_icon_description),
                modifier = Modifier
                    .clickable {
                        showPlaySpeedDialog.value = true
                    }
                    .size(48.dp)
                    .padding(horizontal = 10.dp)
            )
            Icon(
                Icons.Default.Bookmarks,
                contentDescription = stringResource(id = R.string.bookmarks_icon_description),
                modifier = Modifier
                    .clickable {
                        component.onNotesButtonClick()
                    }
                    .size(48.dp)
                    .padding(horizontal = 10.dp)
            )
        })

    if (showPlaySpeedDialog.value) {
        PlaySpeedChangeDialog(
            currentSpeed = component.getPlaySpeed(),
            showPlaySpeedDialog,
            component::onPlaySpeedChange
        )
    }
    if (showManualSleepTimerConfigurationDialog.value) {
        ManualSleepTimerConfigurationDialog(
            settings.sleepTimerType,
            showManualSleepTimerConfigurationDialog,
            component::onManualSleep
        )
    }
}

@Composable
fun ManualSleepTimerConfigurationDialog(
    sleepTimerType: SettingsEntity.SleepTimerType,
    showDialog: MutableState<Boolean>,
    onConfigured: (SettingsEntity.SleepTimerType, Boolean) -> Unit
) {
    val currentClickedItemIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val timerTime = remember {
        mutableStateOf(sleepTimerType)
    }
    val textFieldValue = rememberSaveable {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        confirmButton = {
            Column(
                modifier = Modifier
                    .clickable {
                        showDialog.value = false
                        onConfigured(timerTime.value, false)
                    }
                    .size(48.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(id = R.string.ok))
            }
        },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val text = when(timerTime.value) {
                        is SettingsEntity.SleepTimerType.Common -> {
                            formatDuration((timerTime.value as SettingsEntity.SleepTimerType.Common).time)
                        }
                        SettingsEntity.SleepTimerType.EndOfTheChapter -> {
                            stringResource(id = R.string.the_end_of_the_chapter)
                        }
                    }
                    Text(stringResource(id = R.string.current) + " $text")
                }
                LazyVerticalGrid(columns = GridCells.Fixed(3), content = {
                    items(8) { index ->
                        SleepTimerItem(
                            time = 60000L * 15 * (index + 1),
                            isClicked = currentClickedItemIndex.value == index,
                            onClick = {
                                currentClickedItemIndex.value = index
                                timerTime.value = SettingsEntity.SleepTimerType.Common(it)
                            })
                    }
                    item {
                        Column(
                            modifier =
                            Modifier
                                .width(60.dp)
                                .height(50.dp)
                                .clickable {
                                    currentClickedItemIndex.value = 8
                                    timerTime.value = SettingsEntity.SleepTimerType.EndOfTheChapter
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (currentClickedItemIndex.value != 8) {
                                Text(stringResource(id = R.string.the_end_of_the_chapter),
                                    textAlign = TextAlign.Center)
                            } else {
                                Text(
                                    stringResource(id = R.string.the_end_of_the_chapter),
                                    style = TextStyle(color = Color.Blue),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                })
                TextField(
                    value = textFieldValue.value,
                    onValueChange = {
                        currentClickedItemIndex.value = 9
                        if (it.isNotEmpty()) {
                            textFieldValue.value = it
                            if (it.toLong() < 24 * 60) {
                                val value = it.toLong()
                                timerTime.value = SettingsEntity.SleepTimerType.Common(value * 60000)
                                textFieldValue.value = value.toString()

                            } else {
                                val value = 24 * 60L
                                timerTime.value = SettingsEntity.SleepTimerType.Common(value * 60000)
                                textFieldValue.value = value.toString()
                            }
                        } else {
                            textFieldValue.value = ""
                            timerTime.value = SettingsEntity.SleepTimerType.Common(15 * 60000)
                        }
                    },
                    label = { Text(stringResource(id = R.string.your_value_minuter)) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }
    )
}

@Composable
fun SleepTimerItem(
    time: Long,
    isClicked: Boolean,
    onClick: (Long) -> Unit
) {
    Column(
        modifier =
        Modifier
            .width(60.dp)
            .height(50.dp)
            .clickable {
                onClick(time)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!isClicked) {
            Text(formatDuration(time))
        } else {
            Text(formatDuration(time), style = TextStyle(color = Color.Blue))
        }
    }
}

@Composable
fun PlaySpeedChangeDialog(
    currentSpeed: Float,
    showDialog: MutableState<Boolean>,
    onPlaySpeedChange: (Float) -> Unit
) {
    val useNewValue = rememberSaveable { mutableStateOf(false) }
    val newValue = rememberSaveable { mutableStateOf(currentSpeed) }

    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        confirmButton = {
            Icon(
                Icons.Default.Save,
                stringResource(id = R.string.save),
                modifier = Modifier
                    .clickable {
                        onPlaySpeedChange(newValue.value)
                        showDialog.value = false
                    }
                    .size(48.dp)
                    .padding(10.dp)
            )
        },
        text = {
            Column {
                Slider(
                    value = newValue.value,
                    onValueChange = {
                        useNewValue.value = true
                        newValue.value = it
                    },
                    valueRange = 0.25f.rangeTo(3f),
                    steps = 10,
                    onValueChangeFinished = {
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("x " + String.format("%.2f", newValue.value))
                }
            }
        }
    )
}