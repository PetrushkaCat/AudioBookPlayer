package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import android.util.Log
import cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval
import cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.first

class SaveStatisticsUseCase(
    private val statisticsRepository: StatisticsRepository
) {

    //TODO change to listened interval
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
            } else {
                statisticsRepository.save(
                    StatisticsEntity(
                        prevDayYear,
                        prevDayMonth,
                        prevDayDay,
                        prev.listenedTime + (24 * 60 * 60000 - 1 - interval.startTime),
                        ListenedIntervals(
                            prev.listenedIntervals.intervals +
                                    ListenedInterval(
                                        interval.startTime,
                                        24 * 60 * 60000 - 1,
                                        interval.bookName
                                    )
                        )
                    )
                )

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

            }
            return
        }

        val old = statisticsRepository
            .getDetails(year, month, day)
            .first()

        if (old == null) {
            Log.d("statistics", "db was empty")
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
            Log.d("statistics", old.toString())
            val new = StatisticsEntity(
                old.year,
                old.month,
                old.day,
                old.listenedTime + interval.endTime - interval.startTime,
                ListenedIntervals(old.listenedIntervals.intervals + interval)
            )
            Log.d("statistics", new.toString())
            statisticsRepository.save(new)
        }
    }
}