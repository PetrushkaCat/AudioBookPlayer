package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class GetStatisticsDetailsUseCase(
    private val statisticsRepository: StatisticsRepository
) {

    suspend operator fun invoke(year: Int, month: Int, day: Int): Flow<StatisticsEntity> {
        return statisticsRepository.getDetails(year, month, day)
    }
}