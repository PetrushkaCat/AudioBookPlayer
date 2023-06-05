package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.app.util.formatDurationAlt
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.list.StatisticsListComponent
import cat.petrushkacat.audiobookplayer.domain.models.Month
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun StatisticsListComponentUi(component: StatisticsListComponent) {

    val scope = rememberCoroutineScope()
    val model by component.models.collectAsState()
    val months by component.months.collectAsState()
    val month by component.month.collectAsState()

    val years = months.map {
        it.year
    }.toSet().toList()

    val currentYear = rememberSaveable { mutableStateOf<Int?>(null) }

    val monthsOfYear = months.filter {
        it.year == currentYear.value
    }.sortedByDescending {
        it.month
    }
    val monthStatistics = model.filter {
        it.year == month?.year && it.month == month?.month
    }

    val yearsState = rememberLazyListState()
    val monthsState = rememberLazyListState()

    Column() {
        CommonTopAppBar(title = stringResource(id = R.string.statistics), onBack = {
            scope.launch {
                unselectYear(component, currentYear, component::onBack)
            }
        })
        LazyRow(state = yearsState) {
        items(years.size) { index ->
                var totalTime = 0L
                model
                    .filter { item ->
                        item.year == years[index]
                    }
                    .forEach { item ->
                        totalTime += item.listenedTime
                    }
                StatisticsYearItem(
                    year = years[index],
                    isCurrent = years[index] == currentYear.value,
                    totalTimeListened = totalTime,
                    onClick = { year ->
                        if(year != currentYear.value) {
                            component.onMonthSelect(Month(years[index],
                                months
                                    .filter { it.year == year }
                                    .maxOf { it.month })
                            )
                            currentYear.value = year
                        } else {
                            unselectYear(component, currentYear)
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyRow(state = monthsState) {
            items(monthsOfYear.size) { index ->
                var totalTime = 0L
                model
                    .filter { item ->
                        item.year == monthsOfYear[index].year && item.month == monthsOfYear[index].month
                    }
                    .forEach { item ->
                        totalTime += item.listenedTime
                    }
                StatisticsMonthItem(
                    month = monthsOfYear[index],
                    isCurrent = monthsOfYear[index] == month,
                    totalTimeListened = totalTime,
                    onClick = { month ->
                        component.onMonthSelect(month)
                    }
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(monthStatistics.size) {
                val index = (it - monthStatistics.size).absoluteValue - 1
                StatisticsListItem(
                    modifier = Modifier.clickable {
                        component.onStatisticsSelect(
                            monthStatistics[index].year,
                            monthStatistics[index].month,
                            monthStatistics[index].day
                        )
                    },
                    index = index + 1,
                    date = LocalDate.of(
                        monthStatistics[index].year,
                        monthStatistics[index].month,
                        monthStatistics[index].day
                    ).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
                    timeListened = monthStatistics[index].listenedTime
                )
            }
        }
    }

    BackHandler() {
        unselectYear(component, currentYear, component::onBack)
    }

    LaunchedEffect(key1 = true) {
        try {
            yearsState.scrollToItem(years.indexOf(month?.year))
            monthsState.scrollToItem(monthsOfYear.indexOf(month))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun StatisticsMonthItem(
    month: Month,
    totalTimeListened: Long,
    isCurrent: Boolean,
    onClick: (Month) -> Unit
) {
    val context = LocalContext.current
    val color = if(isCurrent) Color.Blue else contentColorFor(MaterialTheme.colorScheme.background)
    val shape = RoundedCornerShape(16.dp)
    Column(modifier = Modifier
        .padding(horizontal = 4.dp)
        .clip(shape)
        .clickable {
            onClick(month)
        }
        .background(color = Color.Transparent, shape)
        .border(1.dp, color = color, shape = shape)
        .defaultMinSize(minWidth = 80.dp)
        .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(java.time.Month.of(month.month).getDisplayName(
            TextStyle.FULL_STANDALONE,
            Locale(androidx.compose.ui.text.intl.Locale.current.language))
        )
        Text(formatDurationAlt(totalTimeListened, context))
    }
}

@Composable
fun StatisticsYearItem(
    year: Int,
    isCurrent: Boolean,
    totalTimeListened: Long,
    onClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val color = if(isCurrent) Color.Blue else contentColorFor(MaterialTheme.colorScheme.background)
    val shape = RoundedCornerShape(16.dp)

    Column(modifier = Modifier
        .padding(horizontal = 4.dp)
        .clip(shape)
        .clickable {
            onClick(year)
        }
        .border(1.dp, color = color, shape = shape)
        .defaultMinSize(minWidth = 80.dp)
        .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(year.toString())
        Text(formatDurationAlt(totalTimeListened, context))
    }
}

@Composable
fun StatisticsListItem(
    modifier: Modifier,
    index: Int,
    date: String,
    timeListened: Long
) {

    val context = LocalContext.current

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
                Text("${stringResource(id = R.string.total)} ${formatDurationAlt(timeListened, context)}")
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

private fun unselectYear(
    component: StatisticsListComponent,
    currentYear: MutableState<Int?>,
    onBack: () -> Unit = {}
) {
    val date = LocalDate.now()
    val localMonth = Month(date.year, date.monthValue)
    component.onMonthSelect(localMonth)
    currentYear.value = null
    onBack()
}