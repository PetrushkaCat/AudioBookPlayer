package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class GetAllStatisticsInMonthUseCase(
    private val statisticsRepository: StatisticsRepository
) {

    suspend operator fun invoke(month: Month): Flow<List<StatisticsEntity>> {
        return statisticsRepository.getAllStatisticsInMonth(month.year, month.month)
    }

}