package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval
import cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.first

class SaveStatisticsUseCase(
    private val statisticsRepository: StatisticsRepository
) {
    suspend operator fun invoke(
        interval: ListenedInterval,
        year: Int, month: Int, day: Int,
        prevDayYear: Int, prevDayMonth: Int, prevDayDay: Int
    ) {
        val prev = statisticsRepository
            .getDetails(prevDayYear, prevDayMonth, prevDayDay)
            .first()

        if (interval.endTime < interval.startTime) {
            if (prev == null) {
                statisticsRepository.save(
                    StatisticsEntity(
                        prevDayYear,
                        prevDayMonth,
                        prevDayDay,
                        (24 * 60 * 60000 - 1 - interval.startTime),
                        ListenedIntervals(
                            listOf(
                                ListenedInterval(
                                    interval.startTime,
                                    24 * 60 * 60000 - 1,
                                    interval.bookName
                                )
                            )
                        )
                    )
                )
            } else {
                statisticsRepository.save(
                    StatisticsEntity(
                        prevDayYear,
                        prevDayMonth,
                        prevDayDay,
                        prev.listenedTime + (24 * 60 * 60000 - 1 - interval.startTime),
                        ListenedIntervals(
                            prev.listenedIntervals.intervals + ListenedInterval(
                                interval.startTime,
                                24 * 60 * 60000 - 1,
                                interval.bookName
                            )
                        )
                    )
                )
            }

            statisticsRepository.save(
                StatisticsEntity(
                    year,
                    month,
                    day,
                    interval.endTime,
                    ListenedIntervals(
                        listOf(
                            ListenedInterval(
                                0,
                                interval.endTime,
                                interval.bookName
                            )
                        )
                    )
                )
            )
            return
        }

        val old = statisticsRepository
            .getDetails(year, month, day)
            .first()

        if (old == null) {
            statisticsRepository.save(
                StatisticsEntity(
                    year,
                    month,
                    day,
                    interval.endTime - interval.startTime,
                    ListenedIntervals(listOf(interval))
                )
            )
        } else {
            val new = StatisticsEntity(
                old.year,
                old.month,
                old.day,
                old.listenedTime + interval.endTime - interval.startTime,
                ListenedIntervals(old.listenedIntervals.intervals + interval)
            )
            statisticsRepository.save(new)
        }
    }
}