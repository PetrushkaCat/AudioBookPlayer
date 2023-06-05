package cat.petrushkacat.audiobookplayer.components.components.main.statistics

import cat.petrushkacat.audiobookplayer.components.components.main.statistics.details.StatisticsDetailsComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.list.StatisticsListComponentImpl
import cat.petrushkacat.audiobookplayer.components.util.toStateFlow
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

class StatisticsComponentImpl(
    componentContext: ComponentContext,
    private val useCasesProvider: UseCasesProvider,
    private val onBackClicked: () -> Unit
) : StatisticsComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, StatisticsComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.List,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ) = when (config) {
        ChildConfig.List -> {
            StatisticsComponent.Child.StatisticsList(
                StatisticsListComponentImpl(
                    componentContext = componentContext,
                    getAllStatisticsUseCase = useCasesProvider.statisticsUseCases.getAllStatisticsUseCase,
                    getAllStatisticsInMonthUseCase = useCasesProvider.statisticsUseCases.getAllStatisticsInMonthUseCase,
                    getAllMonthsUseCase = useCasesProvider.statisticsUseCases.getAllMonthsUseCase,
                    onStatisticsSelected = { year, month, day ->
                        navigation.push(ChildConfig.Details(year, month, day))
                    },
                    onBackClicked = onBackClicked
                )
            )
        }

        is ChildConfig.Details -> {
            StatisticsComponent.Child.StatisticsDetails(
                StatisticsDetailsComponentImpl(
                    componentContext = componentContext,
                    year = config.year,
                    month = config.month,
                    day = config.day,
                    getStatisticsDetailsUseCase = useCasesProvider.statisticsUseCases.getStatisticsDetailsUseCase,
                    onBackClicked = {
                        navigation.pop()
                    }
                )
            )
        }
    }

    private sealed interface ChildConfig : Parcelable {

        @Parcelize
        object List : ChildConfig

        @Parcelize
        data class Details(val year: Int, val month: Int, val day: Int) : ChildConfig
    }
}