package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.StatisticsComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children

@Composable
fun StatisticsComponentUi(component: StatisticsComponent) {
    val childStack by component.childStack.collectAsState()

    Children(stack = childStack) {
        when(val instance = it.instance) {
            is StatisticsComponent.Child.StatisticsList -> StatisticsListComponentUi(component = instance.component)
            is StatisticsComponent.Child.StatisticsDetails -> StatisticsDetailsComponentUi(component = instance.component)
        }
    }
}