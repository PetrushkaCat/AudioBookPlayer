package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class GetAllStatisticsUseCase(
    private val statisticsRepository: StatisticsRepository
) {

    suspend operator fun invoke(): Flow<List<StatisticsEntity>> {
        return statisticsRepository.getAll()
    }
}