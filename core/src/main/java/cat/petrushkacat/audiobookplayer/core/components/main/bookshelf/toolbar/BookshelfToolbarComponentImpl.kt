package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar

import com.arkivanov.decompose.ComponentContext

class BookshelfToolbarComponentImpl(
    componentContext: ComponentContext,
    private val onFolderButtonClicked: () -> Unit
) : BookshelfToolbarComponent, ComponentContext by componentContext {

    override fun onFolderButtonClick() {
        onFolderButtonClicked()
    }

}