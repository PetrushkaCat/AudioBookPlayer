package cat.petrushkacat.audiobookplayer.domain.models

data class BookNotesEntity(
    val folderName: String = "",
    val duration: Long = 0L,
    val currentChapter: Int = 0,
    val currentChapterTime: Long = 0L,
    val notes: Notes = Notes(emptyList())
)