package cat.petrushkacat.audiobookplayer.data.db.type_converters

import android.util.Log
import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO
import kotlinx.serialization.json.Json

class SettingsTypeConverters {

    @TypeConverter
    fun sleepTimerTypeToJson(sleepTimerType: SettingsEntityDTO.SleepTimerType): String {
        return Json.encodeToString(
            serializer = SettingsEntityDTO.SleepTimerType.serializer(),
            value = sleepTimerType)
    }

    @TypeConverter
    fun fromJsonToSleepTimerType(json: String): SettingsEntityDTO.SleepTimerType {
        Log.d("settings income",json)
        return Json.decodeFromString<SettingsEntityDTO.SleepTimerType>(json)
    }
}