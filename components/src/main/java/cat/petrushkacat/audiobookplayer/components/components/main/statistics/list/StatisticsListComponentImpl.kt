package cat.petrushkacat.audiobookplayer.components.components.main.statistics.list

import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllMonthsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllStatisticsInMonthUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllStatisticsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class StatisticsListComponentImpl(
    componentContext: ComponentContext,
    getAllStatisticsUseCase: GetAllStatisticsUseCase,
    getAllStatisticsInMonthUseCase: GetAllStatisticsInMonthUseCase,
    getAllMonthsUseCase: GetAllMonthsUseCase,
    private val onStatisticsSelected: (Int, Int, Int) -> Unit,
    private val onBackClicked: () -> Unit
) : StatisticsListComponent, ComponentContext by componentContext {

    private val scopeDefault = componentCoroutineScopeDefault()
    private val _models = MutableStateFlow<List<StatisticsEntity>>(emptyList())
    override val models: StateFlow<List<StatisticsEntity>> = _models.asStateFlow()

    override val month: StateFlow<Month?> = _month.asStateFlow()

    private val _months = MutableStateFlow<List<Month>>(emptyList())
    override val months: StateFlow<List<Month>> = _months.asStateFlow()

    init {
        scopeDefault.launch {
            val date = LocalDate.now()
            val month = Month(date.year, date.monthValue)
            if (_month.value == null) {
                _month.value = month
            }
            launch {
                getAllMonthsUseCase.invoke().collect {
                    _months.value = it
                }
            }
            launch {
                _models.value = getAllStatisticsUseCase().first()
            }
        }
    }

    override fun onStatisticsSelect(year: Int, month: Int, day: Int) {
        onStatisticsSelected(year, month, day)
    }

    override fun onMonthSelect(month: Month) {
        _month.value = month
    }

    override fun onBack() {
        onBackClicked()
    }

    companion object {
        private val _month = MutableStateFlow<Month?>(null)
    }
}