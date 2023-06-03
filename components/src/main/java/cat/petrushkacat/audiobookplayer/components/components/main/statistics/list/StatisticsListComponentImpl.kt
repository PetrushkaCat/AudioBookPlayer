package cat.petrushkacat.audiobookplayer.components.components.main.statistics.list

import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllStatisticsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsListComponentImpl(
    componentContext: ComponentContext,
    getAllStatisticsUseCase: GetAllStatisticsUseCase,
    private val onStatisticsSelected: (Int, Int, Int) -> Unit,
    private val onBackClicked: () -> Unit
) : StatisticsListComponent, ComponentContext by componentContext {

    private val scopeDefault = componentCoroutineScopeDefault()
    private val _models = MutableStateFlow<List<StatisticsEntity>>(emptyList())
    override val models: StateFlow<List<StatisticsEntity>> = _models.asStateFlow()

    init {
        scopeDefault.launch {
            getAllStatisticsUseCase.invoke().collect {
                _models.value = it
            }
        }
    }

    override fun onStatisticsSelect(year: Int, month: Int, day: Int) {
        onStatisticsSelected(year, month, day)
    }

    override fun onBack() {
        onBackClicked()
    }
}