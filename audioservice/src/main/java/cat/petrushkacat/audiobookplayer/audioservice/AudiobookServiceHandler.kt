package cat.petrushkacat.audiobookplayer.audioservice

import android.annotation.SuppressLint
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.domain.models.Chapter
import cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval
import cat.petrushkacat.audiobookplayer.domain.models.Note
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.books.AddNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.SaveStatisticsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.util.GregorianCalendar
import javax.inject.Inject

class AudiobookServiceHandler @Inject constructor(
    private val player: ExoPlayer,
    private val sensorListener: SensorListener,
    private val saveStatisticsUseCase: SaveStatisticsUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getBookUseCase: GetBookUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : Player.Listener {

    private val _currentTimings = MutableStateFlow(CurrentTimings(player.currentPosition, player.currentMediaItemIndex))
    val currentTimings = _currentTimings.asStateFlow()

    val isPlaying = MutableStateFlow(player.isPlaying)

    private val _manualSleepTimerState = MutableStateFlow(ManualSleepTimerState(0, false))
    val manualSleepTimerState = _manualSleepTimerState.asStateFlow()

    private var job: Job

    private var isProgressBeingWatched = false

    private var autoSeekBack: Long = 0
    private var seek: Long = 0
    private var greatSeek: Long = 0

    private var startedTime = 0L
    private lateinit var bookName: String
    private lateinit var bookUri: String
    private lateinit var chapters: List<Chapter>
    private lateinit var noteDescription: String

    init {
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            getSettingsUseCase().collect { settings ->
                autoSeekBack = settings.autoRewindBackTime
                seek = settings.rewindTime
                greatSeek = settings.greatRewindTime
                Log.d("Audiobook service handler init settings", settings.toString())
            }
        }
        player.addListener(this)
        job = Job()
    }

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        player.setMediaItems(mediaItemList)
        player.prepare()
    }

    fun setBookInfo(bookName: String, bookUri: String, chapters: List<Chapter>, noteDescription: String) {
        this.bookName = bookName
        this.bookUri = bookUri
        this.chapters = chapters
        this.noteDescription = noteDescription
    }

    fun isStopped() = player.playbackState != Player.STATE_READY

    fun setTimings(chapterIndex: Int, chapterTime: Long, isInitialization: Boolean = false) {
        if(!isInitialization) {
            player.seekTo(chapterIndex, chapterTime)
            _currentTimings.value = CurrentTimings(chapterTime, chapterIndex)
        } else {
            setTimings(
                chapterIndex = chapterIndex,
                chapterTime = if(chapterTime > autoSeekBack) {
                    chapterTime - autoSeekBack
                } else {
                    0
                }
            )
        }
    }

    fun setPlaySpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }

    fun pause() {
        player.pause()
    }

    fun getPlaySpeed() = player.playbackParameters.speed

    fun startStopManualSleepTimer(sleepTimerType: SettingsEntity.SleepTimerType, haveToStopIfPlaying: Boolean) {
        val sleepAfter = when(sleepTimerType) {
            is SettingsEntity.SleepTimerType.Common -> {
                sleepTimerType.time / player.playbackParameters.speed
            }

            SettingsEntity.SleepTimerType.EndOfTheChapter -> {
                (player.duration - player.currentPosition) / player.playbackParameters.speed
            }
        }.toLong()

        if(!haveToStopIfPlaying) {
            _manualSleepTimerState.value = ManualSleepTimerState(
                sleepAfter,
                true
            )
        } else if(_manualSleepTimerState.value.isActive) {
            _manualSleepTimerState.value = ManualSleepTimerState(
                sleepAfter,
                false
            )
        } else {
            _manualSleepTimerState.value = ManualSleepTimerState(
                sleepAfter,
                true
            )
        }
    }

    suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            is PlayerEvent.Backward -> {
                player.seekTo(
                    if(currentTimings.value.currentTimeInChapter > seek)
                        currentTimings.value.currentTimeInChapter - seek
                    else 0
                )
            }
            is PlayerEvent.Forward -> {
                player.seekTo(currentTimings.value.currentTimeInChapter + seek)
            }
            PlayerEvent.PlayPause -> {
                if (player.isPlaying) {
                    player.pause()
                    player.seekTo(
                        if(currentTimings.value.currentTimeInChapter > autoSeekBack)
                            currentTimings.value.currentTimeInChapter - autoSeekBack
                        else 0
                    )
                    stopProgressUpdate()
                } else {
                    Log.d("player-1", "playing")
                    player.play()
                    startProgressUpdate()
                }
            }

            PlayerEvent.NextChapter -> {
                player.seekToNextMediaItem()
            }
            PlayerEvent.PreviousChapter -> {
                player.seekToPreviousMediaItem()
            }
            is PlayerEvent.UpdateProgress -> {
                player.seekTo((player.duration * playerEvent.newProgress).toLong())
            }
            is PlayerEvent.ChooseChapter -> {
                player.seekTo(playerEvent.chapterId, 0)
            }

            PlayerEvent.GreatBackward -> {
                player.seekTo(
                    if(currentTimings.value.currentTimeInChapter > greatSeek)
                        currentTimings.value.currentTimeInChapter - greatSeek
                    else 0
                )
            }
            PlayerEvent.GreatForward -> {
                player.seekTo(currentTimings.value.currentTimeInChapter + greatSeek)
            }
        }

        _currentTimings.value = CurrentTimings(player.currentPosition, player.currentMediaItemIndex)
    }

    @SuppressLint("SwitchIntDef")
    override fun onPlaybackStateChanged(playbackState: Int) {

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            CoroutineScope(Dispatchers.Main).launch {
                startProgressUpdate()
            }
            updateNote()
        } else {
            stopProgressUpdate()
        }
        CoroutineScope(Dispatchers.Main).launch {
            delay(300)
            this@AudiobookServiceHandler.isPlaying.value = player.isPlaying
        }
    }

    fun stopProgressUpdate() {
        Log.d("player-2", "progress is now not updating")
        job.cancel()
        isProgressBeingWatched = false
    }

    private suspend fun startProgressUpdate() {
        job = Job()
        CoroutineScope(job).launch {
            withContext(Dispatchers.Main) {
                Log.d("player-2", "progress is now updating")
                if(!isProgressBeingWatched) {
                    isProgressBeingWatched = true
                    startedTime = LocalTime.now().toNanoOfDay() / 1000000
                    
                    while (job.isActive) {
                        updateTimings()
                    }
                    
                    updateStatistics()
                }
            }
        }
    }
    
    private suspend fun updateTimings() {
        delay(500)
        _currentTimings.value = CurrentTimings(player.currentPosition, player.currentMediaItemIndex)
        if(GregorianCalendar().timeInMillis >= sensorListener.timeToStop) {
            player.pause()
            stopProgressUpdate()
        }
        val sleepAfter = manualSleepTimerState.value.sleepAfter - 500
        _manualSleepTimerState.value = _manualSleepTimerState.value.copy(sleepAfter = sleepAfter)
        if(sleepAfter <= 0 && _manualSleepTimerState.value.isActive) {
            _manualSleepTimerState.value = _manualSleepTimerState.value.copy(sleepAfter = 0, isActive = false)
            player.pause()
        }
    }
    
    private suspend fun updateStatistics() {
        withContext(Dispatchers.IO) {
            val date = LocalDate.now()
            val prevDay = date.minusDays(1)
            saveStatisticsUseCase(
                ListenedInterval(
                    startedTime,
                    LocalTime.now().toNanoOfDay() / 1000000,
                    bookName
                ),
                date.year,
                date.monthValue,
                date.dayOfMonth,
                prevDay.year,
                prevDay.monthValue,
                prevDay.dayOfMonth
            )
        }
    }

    private fun updateNote() {
        CoroutineScope(Dispatchers.IO).launch {
            val index: Int
            val time: Long

            withContext(Dispatchers.Main) {
                index = player.currentMediaItemIndex
                time = player.currentPosition
            }

            try {
                val note = getBookUseCase(bookUri).first().notes.notes.firstOrNull {
                    it.description == noteDescription
                }
                Log.d("note", note.toString())
                if (note != null) {
                    updateNoteUseCase(
                        note,
                        bookUri,
                        noteDescription,
                        index,
                        time
                    )
                } else {
                    val newNote = Note(
                        index,
                        chapters[index].name,
                        time,
                        noteDescription
                    )
                    addNoteUseCase(newNote, bookUri)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class CurrentTimings(
    val currentTimeInChapter: Long,
    val currentChapterIndex: Int,
)

data class ManualSleepTimerState(
    val sleepAfter: Long,
    val isActive: Boolean
)

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object Backward : PlayerEvent()
    object Forward : PlayerEvent()
    object NextChapter: PlayerEvent()
    object PreviousChapter: PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()

    data class ChooseChapter(val chapterId: Int): PlayerEvent()

    object GreatBackward: PlayerEvent()

    object GreatForward: PlayerEvent()
}