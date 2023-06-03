package cat.petrushkacat.audiobookplayer.components.components.main.statistics

import cat.petrushkacat.audiobookplayer.components.components.main.statistics.details.StatisticsDetailsComponent
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.list.StatisticsListComponent
import com.arkivanov.decompose.router.stack.ChildStack
import kotlinx.coroutines.flow.StateFlow

interface StatisticsComponent {

    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        data class StatisticsList(val component: StatisticsListComponent): Child
        data class StatisticsDetails(val component: StatisticsDetailsComponent): Child
    }
}