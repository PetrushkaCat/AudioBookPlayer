package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity

@TypeConverters(value = [AudiobookTypeConverters::class])
@Database(entities = [BookEntity::class, RootFolderEntity::class, SettingsEntity::class], version = 1,
/*    autoMigrations = [AutoMigration(
        from = 1,
        to = 2
    )],
    exportSchema = true*/
)
abstract class AudiobooksDatabase: RoomDatabase() {
    abstract fun audiobooksDao(): AudiobooksDao

    abstract fun rootFoldersDao(): RootFoldersDao

    abstract fun timeUpdateDao(): TimeUpdateDao

    abstract fun settingsDao(): SettingsDao
}