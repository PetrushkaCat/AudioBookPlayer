package cat.petrushkacat.audiobookplayer.data.dto

import androidx.room.Entity
import kotlinx.serialization.Serializable

@Entity(tableName = "StatisticsEntity",
    primaryKeys = ["year", "month", "day"])
data class StatisticsEntityDB(
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