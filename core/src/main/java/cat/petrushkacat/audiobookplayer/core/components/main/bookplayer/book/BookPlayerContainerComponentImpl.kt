package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar.BookPlayerToolbarComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext

class BookPlayerContainerComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    audiobooksRepository: AudiobooksRepository,
    audiobookServiceHandler: AudiobookServiceHandler,
    private val sensorListener: SensorListener,
    private val bookUri: Uri,
    private val onBack: () -> Unit,
    private val onNotesButtonClicked: () -> Unit
) : BookPlayerContainerComponent, ComponentContext by componentContext {

    override val bookPlayerToolbarComponent = BookPlayerToolbarComponentImpl(
        childContext("book_player_toolbar"),
        audiobookServiceHandler,
        audiobooksRepository,
        bookUri,
        onNotesButtonClicked = onNotesButtonClicked
    )


    override val bookPlayerComponent = BookPlayerComponentImpl(
        childContext("book_player_component"),
        context,
        audiobooksRepository,
        audiobookServiceHandler,
        sensorListener,
        bookUri,
        onBack,

    )
}