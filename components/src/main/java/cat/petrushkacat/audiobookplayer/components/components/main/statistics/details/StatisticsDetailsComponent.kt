package cat.petrushkacat.audiobookplayer.components.components.main.statistics.details

import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.StateFlow

interface StatisticsDetailsComponent {

    val models: StateFlow<StatisticsEntity>

    fun onBack()
}