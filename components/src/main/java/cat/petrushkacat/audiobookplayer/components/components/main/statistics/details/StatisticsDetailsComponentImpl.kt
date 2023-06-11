package cat.petrushkacat.audiobookplayer.components.components.main.statistics.details

import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetStatisticsDetailsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsDetailsComponentImpl(
    componentContext: ComponentContext,
    year: Int,
    month: Int,
    day: Int,
    getStatisticsDetailsUseCase: GetStatisticsDetailsUseCase,
    private val onBackClicked: () -> Unit
) : StatisticsDetailsComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()
    private val _models = MutableStateFlow(
        StatisticsEntity(1,1,1,1, ListenedIntervals(emptyList()))
    )
    override val models: StateFlow<StatisticsEntity> = _models.asStateFlow()


    init {
        scopeIO.launch {
            getStatisticsDetailsUseCase.invoke(year, month, day).collect {
                _models.value = it
            }
        }
    }

    override fun onBack() {
        onBackClicked()
    }
}