package cat.petrushkacat.audiobookplayer.components.components.main.statistics.list

import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.StateFlow

interface StatisticsListComponent {

    val models: StateFlow<List<StatisticsEntity>>

    val month: StateFlow<Month?>
    val months: StateFlow<List<Month>>
    fun onStatisticsSelect(year: Int, month: Int, day: Int)
    fun onMonthSelect(month: Month)
    fun onBack()
}