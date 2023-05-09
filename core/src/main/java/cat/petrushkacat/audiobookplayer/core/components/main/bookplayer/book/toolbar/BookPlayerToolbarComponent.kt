package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.toolbar

import kotlinx.coroutines.flow.StateFlow

interface BookPlayerToolbarComponent {


    //val models: StateFlow<Model>

    fun getPlaySpeed(): Float

    fun onPlaySpeedChange(speed: Float)

    fun onNotesButtonClick()

  /*  data class Model(
        val folderName: String = "",
        val duration: Long = 0,
        val playSpeed: Float = 1f,
        val volumeUp: Float = 0f,
        val currentChapter: Int = 0,
        val currentChapterTime: Long = 0
    )*/
}