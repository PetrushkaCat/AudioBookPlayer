package cat.petrushkacat.audiobookplayer.audioservice.model

data class AudioServiceSettings(
    val id: Int = 1,
    val versionCode: Int = 1,
    val autoSleepTime: Long = 600000 * 60 * 2,
    val rewindTime: Long = 15000,
    val autoRewindBackTime: Long = 2000,
)