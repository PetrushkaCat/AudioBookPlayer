package cat.petrushkacat.audiobookplayer.domain.models

import kotlinx.serialization.Serializable

data class StatisticsEntity(
    val year: Int,
    val month: Int,
    val day: Int,
    val listenedTime: Long,
    val listenedIntervals: ListenedIntervals
)

@Serializable
data class ListenedIntervals(
    val intervals: List<ListenedInterval>
)

@Serializable
data class ListenedInterval(
    val startTime: Long,
    val endTime: Long,
    val bookName: String
)

data class Month(
    val year: Int,
    val month: Int
)