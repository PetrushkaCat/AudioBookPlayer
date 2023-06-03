package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.list.StatisticsListComponent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

@Composable
fun StatisticsListComponentUi(component: StatisticsListComponent) {

    val model by component.models.collectAsState()

    Column() {
        CommonTopAppBar(title = stringResource(id = R.string.statistics), onBack = {
            component.onBack()
        })
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(model.size) {
                val index = (it - model.size).absoluteValue - 1
                StatisticsListItem(
                    modifier = Modifier.clickable {
                        component.onStatisticsSelect(
                            model[index].year,
                            model[index].month,
                            model[index].day
                        )
                    },
                    index = index + 1,
                    date = LocalDate.of(
                        model[index].year,
                        model[index].month,
                        model[index].day
                    ).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                    timeListened = model[index].listenedTime
                )
            }
        }
    }
}

@Composable
fun StatisticsListItem(
    modifier: Modifier,
    index: Int,
    date: String,
    timeListened: Long
) {

    val hours: Long = TimeUnit.HOURS.convert(timeListened, TimeUnit.MILLISECONDS)

    val minutes: Long = (TimeUnit.MINUTES.convert(timeListened, TimeUnit.MILLISECONDS)
            - hours * TimeUnit.MINUTES.convert(1, TimeUnit.HOURS))

    val seconds: Long = (TimeUnit.SECONDS.convert(timeListened, TimeUnit.MILLISECONDS)
            - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
            - hours * TimeUnit.SECONDS.convert(1, TimeUnit.HOURS))

    var timeText = ""

    if(hours != 0L) timeText += "${hours}${stringResource(id = R.string.h)} "
    if(minutes != 0L) timeText += "${minutes}${stringResource(id = R.string.m)} "
    if(seconds != 0L) timeText += "${seconds}${stringResource(id = R.string.s)} "

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.size(50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(index.toString())
            }
            Spacer(Modifier.width(30.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(date)
                Spacer(Modifier.height(10.dp))
                Text("${stringResource(id = R.string.total)} $timeText")
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun ItemPreview() {
    StatisticsListItem(
        modifier = Modifier.clickable { },
        index = 1,
        date = "2003 May, 11",
        timeListened = 13240525
    )
}