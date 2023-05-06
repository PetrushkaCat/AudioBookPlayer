package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookMediaService
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.CHAPTER_DURATIONS
import cat.petrushkacat.audiobookplayer.audioservice.DURATION_EXTRA
import cat.petrushkacat.audiobookplayer.audioservice.FOLDER_NAME_EXTRA
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val bookUri: Uri,
    private val onBack: () -> Unit
    ) : BookComponent, ComponentContext by componentContext {

    private val scope = componentContext.componentCoroutineScopeDefault()
    private val scopeMain = componentContext.componentCoroutineScopeMain()

    private val mediaItems: MutableList<MediaItem> = mutableListOf()

    override val currentTimings = audiobookServiceHandler.currentTimings


    override val models: MutableStateFlow<BookEntity> = MutableStateFlow(
        BookEntity(
            bookUri.toString(),
            "null",
            "null",
            Chapters(listOf(Chapter(
                "null",
                "null",
                0,
                "null"
            ))),
            imageUri = null,
            rootFolderUri = bookUri.toString()
        )
    )

    override fun onPlayerEvent(playerEvent: PlayerEvent) {
        scopeMain.launch {
            audiobookServiceHandler.onPlayerEvent(playerEvent)
        }
    }
    init {
       backHandler.register(BackCallback{
               context.stopService(Intent(context, AudiobookMediaService::class.java))
               audiobookServiceHandler.stopProgressUpdate()
               onBack()

       })

        /*scopeMain.launch {
            audiobookServiceHandler.onPlayerEvent(PlayerEvent.PlayPause)
        }*/

        lifecycle.doOnDestroy {
            onDestroy()
        }
         scope.launch {
                audiobooksRepository.getBook(bookUri).collect {
                    models.value = it

                    for (chapter in models.value.chapters.chapters) {
                        val mediaItem = MediaItem.Builder()
                            .setUri(chapter.uri)
                            .setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setFolderType(MediaMetadata.FOLDER_TYPE_ALBUMS)
                                    .setArtworkUri(Uri.parse(models.value.imageUri))
                                    .setAlbumTitle(models.value.name)
                                    .setDisplayTitle(chapter.name)
                                    .build()
                            ).build()

                        mediaItems.add(mediaItem)
                    }
                    Log.d("player-1", "items: " + mediaItems.toString())

                    withContext(Dispatchers.Main) {
                        audiobookServiceHandler.addMediaItemList(mediaItems)
                        audiobookServiceHandler.setTimings(models.value.currentChapter, models.value.currentChapterTime)
                    }

                    startService()
                }
            }

    }

    private fun startService() {

        val chapterDurations: MutableList<Long> = mutableListOf()
        for(chapter in models.value.chapters.chapters) {
            chapterDurations.add(chapter.duration)
        }
        val intent = Intent(context, AudiobookMediaService::class.java)
            .putExtra(FOLDER_NAME_EXTRA, models.value.folderName)
            .putExtra(DURATION_EXTRA, models.value.duration)
            .putExtra(CHAPTER_DURATIONS, chapterDurations.toLongArray())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        }

    }

    private fun onDestroy() {

        val currentChapterTime = audiobookServiceHandler.getPosition()
        var currentTime = currentChapterTime
        val index = audiobookServiceHandler.getCurrentItemIndex()
        var isStarted: Boolean
        var isCompleted: Boolean

        scope.launch {

            repeat(index) {
                currentTime += models.value.chapters.chapters[it].duration
            }

            isStarted = (currentTime > 0)
            isCompleted = (currentTime >= models.value.duration)

            audiobooksRepository.updateBook(
                BookComponent.UpdateInfo(
                    folderName = models.value.folderName,
                    name = models.value.name,
                    currentChapter = index,
                    currentChapterTime = currentChapterTime,
                    currentTime = currentTime,
                    duration = models.value.duration,
                    isStarted = isStarted,
                    isCompleted = isCompleted
                )
            )
        }
    }
}