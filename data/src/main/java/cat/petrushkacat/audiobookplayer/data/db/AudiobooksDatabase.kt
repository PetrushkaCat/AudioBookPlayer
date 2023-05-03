package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cat.petrushkacat.audiobookplayer.data.model.BookEntity
import cat.petrushkacat.audiobookplayer.data.model.RootFolderEntity

@Database(entities = [BookEntity::class, RootFolderEntity::class], version = 1)
abstract class AudiobooksDatabase: RoomDatabase() {
    abstract fun audiobooksDao(): AudiobooksDao

    abstract fun rootFoldersDao(): RootFoldersDao
}