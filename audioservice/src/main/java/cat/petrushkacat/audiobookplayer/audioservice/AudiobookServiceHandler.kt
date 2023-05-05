package cat.petrushkacat.audiobookplayer.audioservice

import android.annotation.SuppressLint
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudiobookServiceHandler @Inject constructor(
    private val player: ExoPlayer
): Player.Listener {

    private val _audiobookMediaState = MutableStateFlow<AudiobookMediaState>(AudiobookMediaState.Initial)
    val audiobookMediaState = _audiobookMediaState.asStateFlow()

    private var job: Job? = null

    init {
        player.addListener(this)
        job = Job()
    }

    fun addMediaItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
        Log.d("player-0", "item: " + mediaItem.toString())
    }

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        Log.d("player-0", "items: " + mediaItemList.toString())
        player.setMediaItems(mediaItemList)
        player.prepare()
        Log.d("player-0", "items: " + mediaItemList.toString())
    }

    suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Backward -> player.seekBack()
            PlayerEvent.Forward -> player.seekForward()
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
            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> player.seekTo((player.duration * playerEvent.newProgress).toLong())
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
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        Log.d("player-2", "progress is now updating")
        while (true) {
            delay(500)
            _audiobookMediaState.value = AudiobookMediaState.Progress(player.currentPosition)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audiobookMediaState.value = AudiobookMediaState.Playing(isPlaying = false)
    }
}

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object Backward : PlayerEvent()
    object Forward : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class AudiobookMediaState {
    object Initial : AudiobookMediaState()
    data class Ready(val duration: Long) : AudiobookMediaState()
    data class Progress(val progress: Long) : AudiobookMediaState()
    data class Buffering(val progress: Long) : AudiobookMediaState()
    data class Playing(val isPlaying: Boolean) : AudiobookMediaState()
}