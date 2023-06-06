package cat.petrushkacat.audiobookplayer.data.mappers

import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO
import cat.petrushkacat.audiobookplayer.domain.models.Grid
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.models.Theme

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
        theme = Theme.valueOf(this.theme.name),
        grid = Grid.valueOf(this.grid.name),
        greatRewindTime = this.greatRewindTime,
        sleepTimerType = sleepTimerType,
        isMaxTimeAutoNoteEnabled = this.isMaxTimeAutoNoteEnabled,
        isOnPlayTapAutoNoteEnabled = this.isOnPlayTapAutoNoteEnabled,
        isReviewButtonEnabled = this.isReviewButtonEnabled,
        isBugReportButtonEnabled = this.isBugReportButtonEnabled
    )
}

fun SettingsEntity.toSettingsEntityDTO(): SettingsEntityDTO {
    val sleepTimerType = when(this.sleepTimerType) {
        is SettingsEntity.SleepTimerType.Common -> {
            SettingsEntityDTO.SleepTimerType.Common(
                (this.sleepTimerType as SettingsEntity.SleepTimerType.Common).time)
        }
        SettingsEntity.SleepTimerType.EndOfTheChapter -> {
            SettingsEntityDTO.SleepTimerType.EndOfTheChapter
        }
    }

    return SettingsEntityDTO(
        id = this.id,
        versionCode = this.versionCode,
        autoSleepTime = this.autoSleepTime,
        rewindTime = this.rewindTime,
        autoRewindBackTime = this.autoRewindBackTime,
        theme = cat.petrushkacat.audiobookplayer.data.dto.Theme.valueOf(this.theme.name),
        grid = cat.petrushkacat.audiobookplayer.data.dto.Grid.valueOf(this.grid.name),
        greatRewindTime = this.greatRewindTime,
        sleepTimerType = sleepTimerType,
        isMaxTimeAutoNoteEnabled = this.isMaxTimeAutoNoteEnabled,
        isOnPlayTapAutoNoteEnabled = this.isOnPlayTapAutoNoteEnabled,
        isReviewButtonEnabled = this.isReviewButtonEnabled,
        isBugReportButtonEnabled = this.isBugReportButtonEnabled
    )
}

/*
fun SettingsEntityDTO.toAudioServiceSettings(): AudioServiceSettings {
    return AudioServiceSettings(
        id = this.id,
        versionCode = this.versionCode,
        autoSleepTime = this.autoSleepTime,
        rewindTime = this.rewindTime,
        autoRewindBackTime = this.autoRewindBackTime,
        greatRewindTime = this.greatRewindTime,
        isOnPlayTapAutoNoteEnabled = this.isOnPlayTapAutoNoteEnabled
    )
}*/
