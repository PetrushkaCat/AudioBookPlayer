package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.StatisticsComponent
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation

@Composable
fun StatisticsComponentUi(component: StatisticsComponent) {
    val childStack by component.childStack.collectAsState()

    Children(
        stack = childStack,
        animation = stackAnimation(fade() + scale())
    ) {
        when(val instance = it.instance) {
            is StatisticsComponent.Child.StatisticsList -> StatisticsListComponentUi(component = instance.component)
            is StatisticsComponent.Child.StatisticsDetails -> StatisticsDetailsComponentUi(component = instance.component)
        }
    }
}