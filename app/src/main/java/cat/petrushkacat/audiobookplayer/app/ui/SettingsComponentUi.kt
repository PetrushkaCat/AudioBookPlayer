package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.core.components.main.settings.SettingsComponent
import cat.petrushkacat.audiobookplayer.core.models.Theme

@Composable
fun SettingsComponentUi(component: SettingsComponent) {

    val model by component.models.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { component.changeTheme(model.theme != Theme.DARK) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Icon(
                    Icons.Default.DarkMode,
                    null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    stringResource(id = R.string.dark_theme),
                    style = TextStyle(fontSize = 17.sp),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Switch(checked = model.theme == Theme.DARK, onCheckedChange = component::changeTheme)
        }
        TimeSettingItem(
            icon = Icons.Default.DoubleArrow,
            name = stringResource(id = R.string.rewind),
            time = model.rewindTime,
            maxTime = 60000,
            minTime = 1000,
            steps = 60,
            onTimeChange = component::changeRewindTime
        )
        TimeSettingItem(
            icon = Icons.Default.RotateLeft,
            name = stringResource(id = R.string.auto_rewind_back_on_pause),
            time = model.autoRewindBackTime,
            maxTime = 30000,
            minTime = 1000,
            steps = 30,
            onTimeChange = component::changeAutoRewindTime
        )
        TimeSettingItem(
            icon = Icons.Default.Bedtime,
            name = stringResource(id = R.string.auto_sleep),
            time = model.autoSleepTime,
            maxTime = 60000 * 60 * 6,
            minTime = 600000,
            steps = 69,
            onTimeChange = component::changeAutoSleepTime
        )
    }
}


@Composable
fun TimeSettingItem(icon: ImageVector, name: String, time: Long, maxTime: Long, minTime: Long, steps: Int, onTimeChange: (Long) -> Unit) {

    val showDialog = rememberSaveable { mutableStateOf(false) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clickable {
            showDialog.value = true
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, modifier = Modifier.align(Alignment.CenterVertically))
        Spacer(modifier = Modifier.width(30.dp))
        Column {
            Text(name, style = TextStyle(fontSize = 17.sp))
            Text(formatDuration(time), style = TextStyle(fontSize = 14.sp, color = Color.Gray))
        }
    }

    if (showDialog.value) {
        ChooseTimeDialog(time, maxTime, minTime, steps, onTimeChange, showDialog)
    }

}

@Composable
fun ChooseTimeDialog(time: Long, maxTime: Long, minTime: Long, steps: Int, onTimeChange: (Long) -> Unit, showDialog: MutableState<Boolean>) {

    val value = rememberSaveable { mutableStateOf(time.toFloat()) }

    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        confirmButton = {
            Icon(Icons.Default.Save,
                contentDescription = stringResource(id = R.string.save),
                modifier = Modifier.clickable {
                    onTimeChange((value.value).toLong())
                    showDialog.value = false
            })
        },
        text = {
            Column {
                Slider(
                    value = value.value,
                    onValueChange = {
                        value.value = it
                    },
                    valueRange = minTime.toFloat().rangeTo(maxTime.toFloat()),
                    steps = steps,
                    onValueChangeFinished = {
                        //useNewValue.value = false
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(formatDuration((value.value).toLong()) + "")
                }
            }
        }
    )
}