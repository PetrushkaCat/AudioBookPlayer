package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer

interface DrawerComponent {

    fun onFavoritesClick()
    fun onListenLaterClick()

    fun onCompletedBooksClick()
    fun onSettingsClick()
    fun onRateClick()

}