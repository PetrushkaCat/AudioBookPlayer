package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class GetAllMonthsUseCase(
    private val statisticsRepository: StatisticsRepository
) {

    suspend operator fun invoke(): Flow<List<Month>> {
        return statisticsRepository.getAllMonth()
    }
}