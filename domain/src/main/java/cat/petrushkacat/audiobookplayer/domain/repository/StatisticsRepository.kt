package cat.petrushkacat.audiobookplayer.domain.repository

import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    suspend fun getAll(): Flow<List<StatisticsEntity>>

    suspend fun getDetails(year: Int, month: Int, day: Int): Flow<StatisticsEntity?>

    suspend fun save(statisticsEntity: StatisticsEntity)

    suspend fun getAllMonth(): Flow<List<Month>>

    suspend fun getAllStatisticsInMonth(year: Int, month: Int): Flow<List<StatisticsEntity>>
}