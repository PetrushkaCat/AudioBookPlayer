package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar

import android.net.Uri
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeMain
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch

class BookPlayerToolbarComponentImpl(
    componentContext: ComponentContext,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val audiobooksRepository: AudiobooksRepository,
    private val bookUri: Uri,
    private val onNotesButtonClicked: () -> Unit,
) : BookPlayerToolbarComponent, ComponentContext by componentContext {

    override fun getPlaySpeed() = audiobookServiceHandler.getPlaySpeed()

    private val scopeMain = componentCoroutineScopeMain()
    private val scopeDefault = componentCoroutineScopeDefault()

    override fun onPlaySpeedChange(speed: Float) {
        scopeMain.launch {
            audiobookServiceHandler.setPlaySpeed(speed)
        }
    }

    override fun onNotesButtonClick() {
        onNotesButtonClicked()
    }
}