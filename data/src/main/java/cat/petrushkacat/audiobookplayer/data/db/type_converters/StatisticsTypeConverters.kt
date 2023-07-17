package cat.petrushkacat.audiobookplayer.data.db.type_converters

import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.data.dto.ListenedIntervals
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class StatisticsTypeConverters {

    @TypeConverter
    fun listenedIntervalsDBToJson(listenedIntervals: ListenedIntervals): String {
        return Json.encodeToString(listenedIntervals)
    }

    @TypeConverter
    fun listenedIntervalsDBFromJson(json: String): ListenedIntervals {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun listenedIntervalsToJson(listenedIntervals: cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals): String {
        return Json.encodeToString(listenedIntervals)
    }

    @TypeConverter
    fun listenedIntervalsFromJson(json: String): cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals {
        return Json.decodeFromString(json)
    }
}