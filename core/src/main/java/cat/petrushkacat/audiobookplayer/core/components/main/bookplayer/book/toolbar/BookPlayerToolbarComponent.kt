package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar

interface BookPlayerToolbarComponent {

    fun getPlaySpeed(): Float

    fun onPlaySpeedChange(speed: Float)

    fun onNotesButtonClick()

    fun onBack()

}