package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer

interface DrawerComponent {

    fun onFavoritesClick()
    fun onListenLaterClick()

    fun onCompletedBooksClick()
    fun onSettingsClick()
    fun onRateClick()

}