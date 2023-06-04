package cat.petrushkacat.audiobookplayer.domain.usecases

import cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval
import cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeStatisticsRepository : StatisticsRepository {

    val fakeStatisticsDB: MutableList<StatisticsEntity> = generateStatistics().toMutableList()

    override suspend fun getAll(): Flow<List<StatisticsEntity>> = flow {
        emit(fakeStatisticsDB)
    }

    override suspend fun getDetails(year: Int, month: Int, day: Int): Flow<StatisticsEntity> =
        flow {
            fakeStatisticsDB.firstOrNull() {
                it.year == year && it.month == month && it.day == day
            }?.let { emit(it) }
        }

    override suspend fun save(statisticsEntity: StatisticsEntity) {
        fakeStatisticsDB.removeIf {
            it.year == statisticsEntity.year
            && it.month == statisticsEntity.month
            && it.day == statisticsEntity.day
        }
        fakeStatisticsDB.add(statisticsEntity)
    }

    private fun generateStatistics(): List<StatisticsEntity> {
        val list = mutableListOf<StatisticsEntity>()

        list.add(
            StatisticsEntity(
                2007, 7, 2, 10000,
                ListenedIntervals(
                    listOf(
                        ListenedInterval(0, 2000, "book1"),
                        ListenedInterval(3000, 11000, "book1")
                    )
                )
            )
        )

        list.add(
            StatisticsEntity(
                2007, 7, 3, 1000,
                ListenedIntervals(
                    listOf(
                        ListenedInterval(86300000, 86301000, "book1"),
                    )
                )
            )
        )

        return list
    }
}