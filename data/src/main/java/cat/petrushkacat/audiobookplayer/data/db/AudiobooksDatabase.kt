package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cat.petrushkacat.audiobookplayer.core.models.BookEntity
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity
import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO

@TypeConverters(value = [AudiobookTypeConverters::class, SettingsTypeConverters::class])
@Database(entities = [BookEntity::class, RootFolderEntity::class, SettingsEntityDTO::class], version = 2,
    autoMigrations = [AutoMigration(
        from = 1,
        to = 2
    )],
    exportSchema = true
)
abstract class AudiobooksDatabase: RoomDatabase() {
    abstract fun audiobooksDao(): AudiobooksDao

    abstract fun rootFoldersDao(): RootFoldersDao

    abstract fun timeUpdateDao(): TimeUpdateDao

    abstract fun settingsDao(): SettingsDao
}