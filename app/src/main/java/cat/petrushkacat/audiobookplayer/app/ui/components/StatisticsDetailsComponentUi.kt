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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.details.StatisticsDetailsComponent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.absoluteValue

@Composable
fun StatisticsDetailsComponentUi(component: StatisticsDetailsComponent) {

    val model by component.models.collectAsState()

    Column() {
        CommonTopAppBar(
            title = LocalDate.of(
                model.year, model.month, model.day
            ).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
            onBack = {
                component.onBack()
            })
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(model.listenedIntervals.intervals.size) {
                val index = (it - model.listenedIntervals.intervals.size).absoluteValue - 1
                val interval = model.listenedIntervals.intervals[index]
                StatisticsDetailsIntervalItem(
                    index = index + 1,
                    startTime = interval.startTime,
                    endTime = interval.endTime,
                    bookName = interval.bookName
                )
            }
        }
    }
}

@Composable
fun StatisticsDetailsIntervalItem(
    modifier: Modifier = Modifier,
    index: Int,
    startTime: Long,
    endTime: Long,
    bookName: String
) {

    Column {
        Row(
            modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(50.dp),
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
                Text(bookName)
                Spacer(Modifier.height(10.dp))
                Text(
                    LocalTime.ofNanoOfDay(startTime * 1000000)
                        .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
                            + " - " +
                        LocalTime.ofNanoOfDay(endTime * 1000000)
                        .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
                )
            }
        }
        Divider(Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun ItemPreview() {
    StatisticsDetailsIntervalItem(
        modifier = Modifier.clickable { },
        index = 1,
        startTime = 111111,
        endTime = 222222,
        bookName = "Book"
    )
}