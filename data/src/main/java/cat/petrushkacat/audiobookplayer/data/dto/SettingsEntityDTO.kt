package cat.petrushkacat.audiobookplayer.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cat.petrushkacat.audiobookplayer.audioservice.model.AudioServiceSettings
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
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

fun SettingsEntityDTO.toSettingsEntity(): SettingsEntity {
    val sleepTimerType = when(this.sleepTimerType) {
        is SettingsEntityDTO.SleepTimerType.Common -> {
            SettingsEntity.SleepTimerType.Common(this.sleepTimerType.time)
        }
        SettingsEntityDTO.SleepTimerType.EndOfTheChapter -> {
            SettingsEntity.SleepTimerType.EndOfTheChapter
        }
    }
    return SettingsEntity(
        id = this.id,
        versionCode = this.versionCode,
        autoSleepTime = this.autoSleepTime,
        rewindTime = this.rewindTime,
        autoRewindBackTime = this.autoRewindBackTime,
        theme = cat.petrushkacat.audiobookplayer.domain.models.Theme.valueOf(this.theme.name),
        grid = cat.petrushkacat.audiobookplayer.domain.models.Grid.valueOf(this.grid.name),
        greatRewindTime = this.greatRewindTime,
        sleepTimerType = sleepTimerType
    )
}

fun SettingsEntityDTO.fromSettingsEntity(settingsEntity: SettingsEntity): SettingsEntityDTO {
    val sleepTimerType = when(settingsEntity.sleepTimerType) {
        is SettingsEntity.SleepTimerType.Common -> {
            SettingsEntityDTO.SleepTimerType.Common(
                (settingsEntity.sleepTimerType as SettingsEntity.SleepTimerType.Common).time)
        }
                SettingsEntity.SleepTimerType.EndOfTheChapter -> {
            SettingsEntityDTO.SleepTimerType.EndOfTheChapter
        }
    }

    return SettingsEntityDTO(
        id = settingsEntity.id,
        versionCode = settingsEntity.versionCode,
        autoSleepTime = settingsEntity.autoSleepTime,
        rewindTime = settingsEntity.rewindTime,
        autoRewindBackTime = settingsEntity.autoRewindBackTime,
        theme = Theme.valueOf(settingsEntity.theme.name),
        grid = Grid.valueOf(settingsEntity.grid.name),
        greatRewindTime = settingsEntity.greatRewindTime,
        sleepTimerType = sleepTimerType
    )
}

fun SettingsEntityDTO.toAudioServiceSettings(): AudioServiceSettings {
    return AudioServiceSettings(
        id = this.id,
        versionCode = this.versionCode,
        autoSleepTime = this.autoSleepTime,
        rewindTime = this.rewindTime,
        autoRewindBackTime = this.autoRewindBackTime,
        greatRewindTime = this.greatRewindTime,
    )
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