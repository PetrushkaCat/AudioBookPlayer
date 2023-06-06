package cat.petrushkacat.audiobookplayer.domain.models

data class SettingsEntity(
    val id: Int = 1,
    val versionCode: Int = 1,
    val autoSleepTime: Long = 60000 * 60 * 2,
    val rewindTime: Long = 15000,
    val autoRewindBackTime: Long = 2000,
    val theme: Theme = Theme.DARK,
    val grid: Grid = Grid.LIST,
    val greatRewindTime: Long = 60000,
    val sleepTimerType: SleepTimerType = SleepTimerType.EndOfTheChapter,
    val isMaxTimeAutoNoteEnabled: Boolean = true,
    val isOnPlayTapAutoNoteEnabled: Boolean = true,
    val isReviewButtonEnabled: Boolean = true,
    val isBugReportButtonEnabled: Boolean = true,
) {
    sealed class SleepTimerType() {

        object EndOfTheChapter: SleepTimerType()

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