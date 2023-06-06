package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer

import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import kotlinx.coroutines.flow.StateFlow

interface DrawerComponent {

    val settings: StateFlow<SettingsEntity>
    val wasRated: StateFlow<Boolean>

    fun onFavoritesClick()
    fun onListenLaterClick()
    fun onCompletedBooksClick()
    fun onSettingsClick()
    fun onRateClick()
    fun onStatisticsClick()
    fun onBugReportClick()

}