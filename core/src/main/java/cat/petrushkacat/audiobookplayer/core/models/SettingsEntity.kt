package cat.petrushkacat.audiobookplayer.core.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val versionCode: Int = 1,
    val autoSleepTime: Long = 60000 * 60 * 2,
    val rewindTime: Long = 15000,
    val autoRewindBackTime: Long = 2000,
    val theme: Theme = Theme.DARK,
    val grid: Grid = Grid.LIST,
    /*@ColumnInfo(defaultValue = "60000") val greatRewindTime: Long = 60000,
    @ColumnInfo(defaultValue = "3600000") val lastManualSleepDuration: Long = 60000 * 60*/
)

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