package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
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
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.components.components.main.settings.SettingsComponent
import cat.petrushkacat.audiobookplayer.domain.models.Theme

@Composable
fun SettingsComponentUi(component: SettingsComponent) {

    val model by component.models.collectAsState()

    Column {
        CommonTopAppBar(title = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.settings), onBack = {
            component.onBack()
        })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SwitchSettingItem(
                icon = Icons.Default.DarkMode,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.dark_theme),
                checked = model.theme == Theme.DARK,
                onClick = component::changeTheme
            )
            SwitchSettingItem(
                icon = Icons.Default.Autorenew,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.show_auto_max_time_note),
                checked = model.isMaxTimeAutoNoteEnabled,
                onClick = component::showMaxTimeAutoNote
            )
            SwitchSettingItem(
                icon = Icons.Default.Autorenew,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.show_auto_on_play_tap_note),
                checked = model.isOnPlayTapAutoNoteEnabled,
                onClick = component::showPlayTapAutoNote
            )
            SwitchSettingItem(
                icon = Icons.Default.StarRate,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.show_review_button),
                checked = model.isReviewButtonEnabled,
                onClick = component::showReviewButton
            )
            SwitchSettingItem(
                icon = Icons.Default.BugReport,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.show_bug_report_button),
                checked = model.isBugReportButtonEnabled,
                onClick = component::showBugReportButton
            )
            Divider(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp))

            TimeSettingItem(
                icon = Icons.Default.KeyboardArrowLeft,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.rewind),
                time = model.rewindTime,
                maxTime = 60000,
                minTime = 1000,
                steps = 60,
                onTimeChange = component::changeRewindTime
            )
            TimeSettingItem(
                icon = Icons.Default.KeyboardDoubleArrowLeft,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.long_rewind),
                time = model.greatRewindTime,
                maxTime = 60000 * 5,
                minTime = 60000,
                steps = 60,
                onTimeChange = component::changeGreatRewindTime
            )
            TimeSettingItem(
                icon = Icons.Default.RotateLeft,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.auto_rewind_back_on_pause),
                time = model.autoRewindBackTime,
                maxTime = 30000,
                minTime = 1000,
                steps = 30,
                onTimeChange = component::changeAutoRewindTime
            )
            TimeSettingItem(
                icon = Icons.Default.Bedtime,
                name = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.auto_sleep),
                time = model.autoSleepTime,
                maxTime = 60000 * 60 * 6,
                minTime = 600000,
                steps = 69,
                onTimeChange = component::changeAutoSleepTime
            )
        }
    }
}

@Composable
fun SwitchSettingItem(
    icon: ImageVector,
    name: String,
    checked: Boolean,
    onClick: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick(!checked) }
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(modifier = Modifier.weight(5f)) {
            Icon(
                icon,
                null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                name,
                style = TextStyle(fontSize = 17.sp),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Switch(
            modifier = Modifier.weight(1f),
            checked = checked,
            onCheckedChange = onClick
        )
    }
}

@Composable
fun TimeSettingItem(icon: ImageVector, name: String, time: Long, maxTime: Long, minTime: Long, steps: Int, onTimeChange: (Long) -> Unit) {

    val showDialog = rememberSaveable { mutableStateOf(false) }

    Row(modifier = Modifier
        .clickable { showDialog.value = true }
        .fillMaxWidth()
        .height(60.dp)
        .padding(horizontal = 16.dp),
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
                contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.save),
                modifier = Modifier
                    .clickable {
                        onTimeChange((value.value).toLong())
                        showDialog.value = false
                    }
                    .size(48.dp)
                    .padding(10.dp)
            )
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
                    onValueChangeFinished = { }
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