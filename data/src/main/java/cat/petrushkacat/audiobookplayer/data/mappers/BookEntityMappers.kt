package cat.petrushkacat.audiobookplayer.data.mappers

import cat.petrushkacat.audiobookplayer.data.dto.BookEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.BookEntity
import cat.petrushkacat.audiobookplayer.domain.models.Chapter
import cat.petrushkacat.audiobookplayer.domain.models.Chapters
import cat.petrushkacat.audiobookplayer.domain.models.Note
import cat.petrushkacat.audiobookplayer.domain.models.Notes

fun BookEntityDB.toBookEntity(): BookEntity {
    val chapters = Chapters(
        this.chapters.chapters.map {
        Chapter(
        bookFolderUri = it.bookFolderUri,
        name = it.name,
        duration = it.duration,
        timeFromBeginning = it.timeFromBeginning,
        uri = it.uri
        )
    })
    val notes = Notes(
        this.notes.notes.map {
            Note(
                chapterIndex = it.chapterIndex,
                chapterName = it.chapterName,
                time = it.time,
                description = it.description
            )
        }
    )
    return BookEntity(
        folderUri = this.folderUri,
        folderName = this.folderName,
        name = this.name,
        chapters = chapters,
        currentChapter = this.currentChapter,
        currentChapterTime = this.currentChapterTime,
        currentTime = this.currentTime,
        duration = this.duration,
        rootFolderUri = this.rootFolderUri,
        image = this.image,
        isStarted = this.isStarted,
        isCompleted = this.isCompleted,
        volumeUp = this.volumeUp,
        playSpeed = this.playSpeed,
        notes = notes,
        lastTimeListened = this.lastTimeListened,
        isFavorite = this.isFavorite,
        isTemporarilyDeleted = this.isTemporarilyDeleted,
        isWantToListen = this.isWantToListen,
        addedTime = this.addedTime
    )
}

fun BookEntity.toBookEntityDB(): BookEntityDB {
    val chapters = cat.petrushkacat.audiobookplayer.data.dto.Chapters(
        this.chapters.chapters.map {
            cat.petrushkacat.audiobookplayer.data.dto.Chapter(
                bookFolderUri = it.bookFolderUri,
                name = it.name,
                duration = it.duration,
                timeFromBeginning = it.timeFromBeginning,
                uri = it.uri
            )
        })
    val notes = cat.petrushkacat.audiobookplayer.data.dto.Notes(
        this.notes.notes.map {
            cat.petrushkacat.audiobookplayer.data.dto.Note(
                chapterIndex = it.chapterIndex,
                chapterName = it.chapterName,
                time = it.time,
                description = it.description
            )
        }
    )
    return BookEntityDB(
        folderUri = this.folderUri,
        folderName = this.folderName,
        name = this.name,
        chapters = chapters,
        currentChapter = this.currentChapter,
        currentChapterTime = this.currentChapterTime,
        currentTime = this.currentTime,
        duration = this.duration,
        rootFolderUri = this.rootFolderUri,
        image = this.image,
        isStarted = this.isStarted,
        isCompleted = this.isCompleted,
        volumeUp = this.volumeUp,
        playSpeed = this.playSpeed,
        notes = notes,
        lastTimeListened = this.lastTimeListened,
        isFavorite = this.isFavorite,
        isTemporarilyDeleted = this.isTemporarilyDeleted,
        isWantToListen = this.isWantToListen,
        addedTime = this.addedTime
    )
}