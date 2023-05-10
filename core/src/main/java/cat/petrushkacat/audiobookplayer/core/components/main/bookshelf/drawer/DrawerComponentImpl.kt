package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer

import com.arkivanov.decompose.ComponentContext

class DrawerComponentImpl(
    componentContext: ComponentContext,
    private val onSettingsClicked: () -> Unit
) : DrawerComponent {

    override fun onSettingsClick() {
        onSettingsClicked()
    }
}