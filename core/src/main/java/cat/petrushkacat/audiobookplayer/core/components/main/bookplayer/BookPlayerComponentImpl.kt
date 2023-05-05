package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.ToolbarComponent
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext

class BookPlayerComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    audiobooksRepository: AudiobooksRepository,
    audiobookServiceHandler: AudiobookServiceHandler,
    private val bookUri: Uri
) : BookPlayerComponent, ComponentContext by componentContext {
    override val toolbarComponent: ToolbarComponent
        get() = TODO("Not yet implemented")

    override val bookComponent = BookComponentImpl(
        childContext("book_component"),
        context,
        audiobooksRepository,
        audiobookServiceHandler,
        bookUri
    )

}