package cat.petrushkacat.audiobookplayer.data.db.type_converters

import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsTypeConverters {

    @TypeConverter
    fun sleepTimerTypeToJson(sleepTimerType: SettingsEntityDTO.SleepTimerType): String {
        return Json.encodeToString(sleepTimerType)
    }

    @TypeConverter
    fun fromJsonToSleepTimerType(json: String): SettingsEntityDTO.SleepTimerType {
        return Json.decodeFromString(json)
    }
}