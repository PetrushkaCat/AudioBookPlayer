package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookMediaService
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.isAudio
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeMain
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val bookUri: Uri
) : BookComponent, ComponentContext by componentContext {

    private val scope = componentContext.componentCoroutineScopeDefault()
    private val scopeMain = componentContext.componentCoroutineScopeMain()

    override val models: MutableStateFlow<BookEntity> = MutableStateFlow(BookEntity(bookUri.toString(), "folderName", "name", Chapters(emptyList()), imageUri = null, rootFolderUri = bookUri.toString()))

    override fun onButtonPrevChapterClick() {
        TODO("Not yet implemented")
    }

    override fun onButtonNextChapterClick() {
        TODO("Not yet implemented")
    }

    override fun onButtonPlusTimeClick() {
        TODO("Not yet implemented")
    }

    override fun onButtonMinusButtonClick() {
        TODO("Not yet implemented")
    }

    override fun onButtonPlayPauseClick() {
        scopeMain.launch {
            audiobookServiceHandler.onPlayerEvent(PlayerEvent.PlayPause)
        }
    }

    override fun onChapterSelectedManually(chapterNumber: Int) {
        TODO("Not yet implemented")
    }

    init {
        scope.launch {
            audiobooksRepository.getBook(bookUri).collect {
                models.value = it

                val mediaItems: MutableList<MediaItem> = mutableListOf()

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
                }
            }
        }
        startService()

    }

    private fun startService() {
       // if (!isServiceRunning) {
        val intent = Intent(context, AudiobookMediaService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        }
        //isServiceRunning = true }
    }
}