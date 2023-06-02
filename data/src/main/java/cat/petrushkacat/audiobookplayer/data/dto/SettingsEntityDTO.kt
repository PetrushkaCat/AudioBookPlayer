package cat.petrushkacat.audiobookplayer.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "SettingsEntity")
data class SettingsEntityDTO(
    @PrimaryKey val id: Int = 1,
    val versionCode: Int = 1,
    val autoSleepTime: Long = 60000 * 60 * 2,
    val rewindTime: Long = 15000,
    val autoRewindBackTime: Long = 2000,
    val theme: Theme = Theme.DARK,
    val grid: Grid = Grid.LIST,
    @ColumnInfo(defaultValue = "60000") val greatRewindTime: Long = 60000,
    @ColumnInfo(defaultValue = "{\"type\":\"cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO.SleepTimerType.EndOfTheChapter\"}")
    val sleepTimerType: SleepTimerType = SleepTimerType.EndOfTheChapter
) {
    @Serializable
    sealed class SleepTimerType() {

        @Serializable
        object EndOfTheChapter: SleepTimerType()

        @Serializable
        class Common(val time: Long): SleepTimerType()
    }
}

enum class Theme {
    //SYSTEM_DEFAULT,
    DARK,
    LIGHT,
}

enum class Grid {
    LIST,
    BIG_CELLS,
    SMALL_CELLS
}