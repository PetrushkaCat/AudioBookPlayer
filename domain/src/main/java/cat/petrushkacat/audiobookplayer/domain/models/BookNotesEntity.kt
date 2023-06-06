package cat.petrushkacat.audiobookplayer.domain.models

data class BookNotesEntity(
    val folderName: String = "",
    val duration: Long = 0L,
    val notes: Notes = Notes(emptyList())
)