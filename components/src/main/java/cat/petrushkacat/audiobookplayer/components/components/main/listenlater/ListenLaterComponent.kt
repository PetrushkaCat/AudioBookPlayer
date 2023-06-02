package cat.petrushkacat.audiobookplayer.components.components.main.listenlater

import cat.petrushkacat.audiobookplayer.components.components.shared.bookdropdownmenu.BookDropdownMenuComponent
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import kotlinx.coroutines.flow.StateFlow

interface ListenLaterComponent {

    val bookDropDownMenuComponent: BookDropdownMenuComponent

    val models: StateFlow<List<BookListEntity>>

    val settings: StateFlow<cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity>

    fun onBack()

    fun onBookClick(bookUri: String)

}