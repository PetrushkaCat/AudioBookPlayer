package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponentImpl
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext

class BookPlayerComponentImpl(
    componentContext: ComponentContext,
    context: Context,
    private val bookUri: Uri
) : BookPlayerComponent, ComponentContext by componentContext {

    override val bookComponent = BookComponentImpl(
        childContext("book_component"),
        context,
        bookUri
    )
}