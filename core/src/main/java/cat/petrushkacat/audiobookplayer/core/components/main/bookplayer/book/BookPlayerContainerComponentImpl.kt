package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.net.Uri
import androidx.core.app.ComponentActivity
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookMediaService
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext

class BookPlayerContainerComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val sensorListener: SensorListener,
    private val bookUri: Uri,
    private val onBack: () -> Unit,
    private val onNotesButtonClicked: () -> Unit,
) : BookPlayerContainerComponent, ComponentContext by componentContext {

    override val bookPlayerToolbarComponent = BookPlayerToolbarComponentImpl(
        childContext("book_player_toolbar"),
        audiobookServiceHandler,
        audiobooksRepository,
        bookUri,
        onNotesButtonClicked = onNotesButtonClicked,
        onBackClicked = {
            doOnBack()
            onBack()
        }
    )


    override val bookPlayerComponent = BookPlayerComponentImpl(
        childContext("book_player_component"),
        context,
        audiobooksRepository,
        audiobookServiceHandler,
        sensorListener,
        bookUri,
        onBack = {
            doOnBack()
            onBack()
        }
    )

    private fun doOnBack() {
        val sensorManager = context.getSystemService(ComponentActivity.SENSOR_SERVICE) as SensorManager

        context.stopService(Intent(context, AudiobookMediaService::class.java))
        audiobookServiceHandler.stopProgressUpdate()
        sensorManager.unregisterListener(sensorListener)
    }
}