package cat.petrushkacat.audiobookplayer.audioservice

import android.annotation.SuppressLint
import android.app.Service
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudiobookServiceHandler @Inject constructor(
    private val player: ExoPlayer,
) : Player.Listener {

    private val _audiobookMediaState =
        MutableStateFlow<AudiobookMediaState>(AudiobookMediaState.Initial)
    val audiobookMediaState = _audiobookMediaState.asStateFlow()

    private val _currentTimings = MutableStateFlow(CurrentTimings(player.currentPosition, player.currentMediaItemIndex))
    val currentTimings = _currentTimings.asStateFlow()

    private var job: Job

    private var isProgressBeingWatched = false

    init {
        player.addListener(this)
        job = Job()
    }

    /*fun addMediaItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
        Log.d("player-0", "item: " + mediaItem.toString())
    }*/

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        Log.d("player-0", "items: " + mediaItemList.toString())
        player.setMediaItems(mediaItemList)
        player.prepare()
        Log.d("player-0", "items: " + mediaItemList.toString())
    }

    fun removeItems() {
        player.stop()
        player.removeMediaItems(0, player.mediaItemCount)
    }

    fun setTimings(chapterIndex: Int, chapterTime: Long) {
        player.seekTo(chapterIndex, chapterTime)
        _currentTimings.value = CurrentTimings(chapterTime, chapterIndex)
    }

    fun getPosition(): Long = player.contentPosition

    fun getCurrentItemIndex() = player.currentMediaItemIndex


    suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            is PlayerEvent.Backward -> {
                player.seekBack()
                _currentTimings.value = CurrentTimings(player.currentPosition, player.currentMediaItemIndex)
            }
            is PlayerEvent.Forward -> {
                player.seekForward()
                _currentTimings.value = CurrentTimings(player.currentPosition, player.currentMediaItemIndex)
            }
            PlayerEvent.PlayPause -> {
                if (player.isPlaying) {
                    player.pause()
                    stopProgressUpdate()
                } else {
                    Log.d("player-1", "playing")
                    player.play()
                    _audiobookMediaState.value = AudiobookMediaState.Playing(isPlaying = true)
                    Log.d("player-1.1", "playing1")
                    startProgressUpdate()
                }
            }

            PlayerEvent.NextChapter -> player.seekToNextMediaItem()
            PlayerEvent.PreviousChapter -> player.seekToPreviousMediaItem()
            is PlayerEvent.UpdateProgress -> player.seekTo((player.duration * playerEvent.newProgress).toLong())
            is PlayerEvent.ChooseChapter -> {
                player.seekTo(playerEvent.chapterId, 0)
                _currentTimings.value = CurrentTimings(player.currentPosition, player.currentMediaItemIndex)
            }
        }
    }

    @SuppressLint("SwitchIntDef")
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audiobookMediaState.value =
                AudiobookMediaState.Buffering(player.currentPosition)

            ExoPlayer.STATE_READY -> _audiobookMediaState.value =
                AudiobookMediaState.Ready(player.duration)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audiobookMediaState.value = AudiobookMediaState.Playing(isPlaying = isPlaying)
        if (isPlaying) {
            CoroutineScope(Dispatchers.Main).launch {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() {
        job = Job()
        job.run {
            withContext(Dispatchers.Main) {
                Log.d("player-2", "progress is now updating")
                if(!isProgressBeingWatched) {
                    isProgressBeingWatched = true
                    while (job.isActive) {
                        delay(500)
                        _currentTimings.value = CurrentTimings(player.currentPosition, player.currentMediaItemIndex)
                    }
                }
            }
        }
    }

    fun stopProgressUpdate() {
        Log.d("player-2", "progress is now not updating")
        job.cancel()
        _audiobookMediaState.value = AudiobookMediaState.Playing(isPlaying = false)
        isProgressBeingWatched = false
    }
}

data class CurrentTimings(
    val currentTimeInChapter: Long,
    val currentChapterIndex: Int
)

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    data class Backward(val time: Long) : PlayerEvent()
    data class Forward(val time: Long) : PlayerEvent()
    object NextChapter: PlayerEvent()
    object PreviousChapter: PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()

    data class ChooseChapter(val chapterId: Int): PlayerEvent()
}

sealed class AudiobookMediaState {
    object Initial : AudiobookMediaState()
    data class Ready(val duration: Long) : AudiobookMediaState()
    data class Progress(val progress: Long) : AudiobookMediaState()
    data class Buffering(val progress: Long) : AudiobookMediaState()
    data class Playing(val isPlaying: Boolean) : AudiobookMediaState()
}