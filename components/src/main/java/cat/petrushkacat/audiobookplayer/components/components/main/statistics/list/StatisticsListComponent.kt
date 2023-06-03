package cat.petrushkacat.audiobookplayer.components.components.main.statistics.list

import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.StateFlow

interface StatisticsListComponent {

    val models: StateFlow<List<StatisticsEntity>>

    fun onStatisticsSelect(year: Int, month: Int, day: Int)

    fun onBack()
}