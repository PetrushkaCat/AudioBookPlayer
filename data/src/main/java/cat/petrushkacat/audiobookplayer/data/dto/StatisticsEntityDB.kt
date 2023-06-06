package cat.petrushkacat.audiobookplayer.data.dto

import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.serialization.Serializable

@Keep
@Entity(tableName = "StatisticsEntity",
    primaryKeys = ["year", "month", "day"])
data class StatisticsEntityDB(
    val year: Int,
    val month: Int,
    val day: Int,
    val listenedTime: Long,
    val listenedIntervals: ListenedIntervals
)

@Keep
@Serializable
data class ListenedIntervals(
    val intervals: List<ListenedInterval>
)

@Keep
@Serializable
data class ListenedInterval(
    val startTime: Long,
    val endTime: Long,
    val bookName: String
)