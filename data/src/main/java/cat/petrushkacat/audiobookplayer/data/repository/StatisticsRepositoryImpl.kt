package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.data.db.dao.StatisticsDao
import cat.petrushkacat.audiobookplayer.data.mappers.toStatisticsEntityDB
import cat.petrushkacat.audiobookplayer.data.mappers.toStatisticsEntityListItem
import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StatisticsRepositoryImpl(
    private val statisticsDao: StatisticsDao
) : StatisticsRepository {

    override suspend fun getAll(): Flow<List<StatisticsEntity>> {
        return statisticsDao.getAll().map {
            it.map { item ->
                item.toStatisticsEntityListItem()
            }
        }
    }

    override suspend fun getDetails(year: Int, month: Int, day: Int): Flow<StatisticsEntity?> {
        return statisticsDao.getDetails(year, month, day)
    }

    override suspend fun save(statisticsEntity: StatisticsEntity) {
        statisticsDao.save(statisticsEntity.toStatisticsEntityDB())
    }

    override suspend fun getAllMonth(): Flow<List<Month>> {
        return statisticsDao.getAllMonths().map {
            it.toSet().toList()
        }
    }

    override suspend fun getAllStatisticsInMonth(year: Int, month: Int): Flow<List<StatisticsEntity>> {
        return statisticsDao.getAllStatisticsInMonth(year, month).map {
            it.map { item ->
                item.toStatisticsEntityListItem()
            }
        }
    }
}