package cat.petrushkacat.audiobookplayer.data.mappers

import cat.petrushkacat.audiobookplayer.data.dto.ListenedInterval
import cat.petrushkacat.audiobookplayer.data.dto.ListenedIntervals
import cat.petrushkacat.audiobookplayer.data.dto.StatisticsEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity


fun StatisticsEntity.toStatisticsEntityDB(): StatisticsEntityDB {
    val listenedIntervals = ListenedIntervals(
        intervals = this.listenedIntervals.intervals.map {
            ListenedInterval(
                startTime = it.startTime,
                endTime = it.endTime,
                bookName = it.bookName
            )
        }
    )
    return StatisticsEntityDB(
        year = this.year,
        month = this.month,
        day = this.day,
        listenedTime = this.listenedTime,
        listenedIntervals = listenedIntervals
    )
}

fun StatisticsEntityDB.toStatisticsEntity(): StatisticsEntity {
    val listenedIntervals = cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals(
        intervals = this.listenedIntervals.intervals.map {
            cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval(
                startTime = it.startTime,
                endTime = it.endTime,
                bookName = it.bookName
            )
        }
    )
    return StatisticsEntity(
        year = this.year,
        month = this.month,
        day = this.day,
        listenedTime = this.listenedTime,
        listenedIntervals = listenedIntervals
    )
}

fun StatisticsEntity.toStatisticsEntityListItem(): StatisticsEntity {
    return StatisticsEntity(
        year = this.year,
        month = this.month,
        day = this.day,
        listenedTime = this.listenedTime,
        listenedIntervals = cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals(
            emptyList()
        )
    )
}