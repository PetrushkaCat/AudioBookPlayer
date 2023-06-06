package cat.petrushkacat.audiobookplayer.data.dto

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Keep
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
    val sleepTimerType: SleepTimerType = SleepTimerType.EndOfTheChapter,
    @ColumnInfo(defaultValue = "1") val isMaxTimeAutoNoteEnabled: Boolean = true,
    @ColumnInfo(defaultValue = "1") val isOnPlayTapAutoNoteEnabled: Boolean = true,
    @ColumnInfo(defaultValue = "1") val isReviewButtonEnabled: Boolean = true,
    @ColumnInfo(defaultValue = "1") val isBugReportButtonEnabled: Boolean = true,
) {
    @Keep
    @Serializable
    sealed class SleepTimerType() {
        @Keep
        @Serializable
        object EndOfTheChapter: SleepTimerType()
        @Keep
        @Serializable
        class Common(val time: Long): SleepTimerType()
    }
}
@Keep
enum class Theme {
    //SYSTEM_DEFAULT,
    DARK,
    LIGHT,
}
@Keep
enum class Grid {
    LIST,
    BIG_CELLS,
    SMALL_CELLS
}