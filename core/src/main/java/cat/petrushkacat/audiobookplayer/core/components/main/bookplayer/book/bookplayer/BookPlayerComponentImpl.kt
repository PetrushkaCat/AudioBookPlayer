package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ComponentActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookMediaService
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.CHAPTER_DURATIONS
import cat.petrushkacat.audiobookplayer.audioservice.DURATION_EXTRA
import cat.petrushkacat.audiobookplayer.audioservice.FOLDER_NAME_EXTRA
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.Chapter
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeMain
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookPlayerComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    sensorListener: SensorListener,
    private val bookUri: Uri,
    private val onBack: () -> Unit,
) : BookPlayerComponent, ComponentContext by componentContext {

    private val stateSaverString = "state_saver_player"

    private var alive = true
    private val scope = componentContext.componentCoroutineScopeDefault()
    private val scopeMain = componentContext.componentCoroutineScopeMain()

    private val mediaItems: MutableList<MediaItem> = mutableListOf()
    private lateinit var sensorManager: SensorManager

    override val isPlaying = audiobookServiceHandler.isPlaying.asStateFlow()

    override val currentTimings = audiobookServiceHandler.currentTimings
    private val _models = MutableStateFlow(
        BookEntity(
            bookUri.toString(),
            "null",
            "null",
            Chapters(
                listOf(
                    Chapter(
                        "null",
                        "null",
                        0,
                        0L,
                        "null"
                    )
                )
            ),
            image = null,
            rootFolderUri = bookUri.toString(),
            lastTimeListened = 0L
        )
    )
    override val models = _models.asStateFlow()

    override fun onPlayerEvent(playerEvent: PlayerEvent) {
        scopeMain.launch {
            audiobookServiceHandler.onPlayerEvent(playerEvent)
        }
    }

    init {
        scope.launch {
            launch {
                Log.d("player-5", isInitialized.toString() + " $counter")
                backHandler.register(BackCallback {
                    onBack()
                })

                lifecycle.doOnDestroy {
                    alive = false
                }
            }
            launch {
                audiobooksRepository.getBook(bookUri).collect {
                    _models.value = it
                    var isStropped: Boolean
                    withContext(Dispatchers.Main) {
                        isStropped = audiobookServiceHandler.isStopped()
                    }
                    Log.d("list null" , it?.toString() ?: "null")
                    Log.d("list null" , it.chapters.chapters?.toString() ?: "null")
                    if (!isInitialized || isStropped) {
                        for (chapter in it.chapters.chapters) {
                            Log.d("list null" , "2")
                            val mediaItem = MediaItem.Builder()
                                .setUri(chapter.uri)
                                .setMediaMetadata(
                                    MediaMetadata.Builder()
                                        .setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
                                        .setArtworkData(
                                            it.image,
                                            MediaMetadata.PICTURE_TYPE_FRONT_COVER
                                        )
                                        .setAlbumTitle(it.name)
                                        .setDisplayTitle(chapter.name)
                                        .build()
                                ).build()

                            mediaItems.add(mediaItem)
                        }
                        Log.d("player-1", "items: " + mediaItems.toString())

                        withContext(Dispatchers.Main) {
                            isInitialized = true
                            audiobookServiceHandler.addMediaItemList(mediaItems)
                            audiobookServiceHandler.setTimings(
                                chapterIndex = it.currentChapter,
                                chapterTime = it.currentChapterTime,
                                isInitialization = true
                            )
                            audiobookServiceHandler.setPlaySpeed(it.playSpeed)
                        }
                        startService(it.chapters, it.folderName, it.duration)
                    }

                }
            }
            Log.d("list null" , "3")

            sensorManager = context.getSystemService(ComponentActivity.SENSOR_SERVICE) as SensorManager
            val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            try {
                sensorManager.registerListener(
                    sensorListener,
                    sensor!!,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("sensor", "no sensor Registered")
            }
        }
    }

    private fun startService(chapters: Chapters, folderName: String, duration: Long) {

        val chapterDurations: MutableList<Long> = mutableListOf()
        for (chapter in chapters.chapters) {
            chapterDurations.add(chapter.duration)
        }
        val intent = Intent(context, AudiobookMediaService::class.java)
            .putExtra(FOLDER_NAME_EXTRA, folderName)
            .putExtra(DURATION_EXTRA, duration)
            .putExtra(CHAPTER_DURATIONS, chapterDurations.toLongArray())

        context.startForegroundService(intent)
    }

    companion object {
        var isInitialized = false
        var counter = 0
    }
}