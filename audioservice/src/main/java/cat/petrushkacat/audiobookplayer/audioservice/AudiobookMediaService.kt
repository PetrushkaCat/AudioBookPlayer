package cat.petrushkacat.audiobookplayer.audioservice

import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import cat.petrushkacat.audiobookplayer.audioservice.notification.AudiobookNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

const val FOLDER_NAME_EXTRA = "folder_name_extra"
const val DURATION_EXTRA = "duration_extra"
const val CHAPTER_DURATIONS = "chapter_durations"
const val SERVICE_CALL_CODE_STOP = 1
const val SERVICE_CALL_CODE_START = 0

@AndroidEntryPoint
class AudiobookMediaService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var notificationManager: AudiobookNotificationManager

    @Inject
    lateinit var timeUpdateRepository: TimeUpdateRepository

    private var folderName: String = ""

    private var duration: Long = 0

    private var chapterDurations: List<Long> = emptyList()

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        folderName = intent?.getStringExtra(FOLDER_NAME_EXTRA) ?: ""
        duration = intent?.getLongExtra(DURATION_EXTRA, 0) ?: 0
        chapterDurations = intent?.getLongArrayExtra(CHAPTER_DURATIONS)?.asList() ?: emptyList()

        notificationManager.startNotificationService(
            mediaSessionService = this,
            mediaSession = mediaSession
        )

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("player-stopped", "")
        mediaSession.run {
            val currentChapter = player.currentMediaItemIndex
            val currentChapterTime = player.currentPosition
            var currentTime = currentChapterTime
            var isStarted = false
            var isCompleted = false
            val playSpeed = player.playbackParameters.speed
            val volume = player.volume

            repeat(currentChapter) {
                currentTime += chapterDurations[it]
            }

            isStarted = (currentTime > 0)
            isCompleted = (currentTime >= duration)

            CoroutineScope(Dispatchers.IO).launch {
                timeUpdateRepository.updateTime(
                    UpdateTime(
                        folderName = folderName,
                        currentChapter = currentChapter,
                        currentChapterTime = currentChapterTime,
                        currentTime = currentTime,
                        duration = duration,
                        isStarted = isStarted,
                        isCompleted = isCompleted,
                        playSpeed = playSpeed,
                        volumeUp = volume,
                        lastTimeListened = GregorianCalendar().timeInMillis
                    )
                )
            }
            release()
            player.pause()
            player.stop()
            player.clearMediaItems()

        }
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession
}

data class UpdateTime(
    val folderName: String,
    val currentChapter: Int,
    val currentChapterTime: Long,
    val currentTime: Long,
    val duration: Long,
    val isStarted: Boolean,
    val isCompleted: Boolean,
    val playSpeed: Float,
    val volumeUp: Float,
    val lastTimeListened: Long,
)