package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity

@TypeConverters(value = [AudiobookTypeConverters::class])
@Database(entities = [BookEntity::class, RootFolderEntity::class], version = 1)
abstract class AudiobooksDatabase: RoomDatabase() {
    abstract fun audiobooksDao(): AudiobooksDao

    abstract fun rootFoldersDao(): RootFoldersDao

    abstract fun timeUpdateDao(): TimeUpdateDao
}