package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cat.petrushkacat.audiobookplayer.data.db.dao.AudiobooksDao
import cat.petrushkacat.audiobookplayer.data.db.dao.RootFoldersDao
import cat.petrushkacat.audiobookplayer.data.db.dao.SettingsDao
import cat.petrushkacat.audiobookplayer.data.db.dao.StatisticsDao
import cat.petrushkacat.audiobookplayer.data.db.dao.TimeUpdateDao
import cat.petrushkacat.audiobookplayer.data.db.type_converters.AudiobookTypeConverters
import cat.petrushkacat.audiobookplayer.data.db.type_converters.SettingsTypeConverters
import cat.petrushkacat.audiobookplayer.data.db.type_converters.StatisticsTypeConverters
import cat.petrushkacat.audiobookplayer.data.dto.BookEntityDB
import cat.petrushkacat.audiobookplayer.data.dto.RootFolderEntityDB
import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO
import cat.petrushkacat.audiobookplayer.data.dto.StatisticsEntityDB

@TypeConverters(value = [
    AudiobookTypeConverters::class,
    SettingsTypeConverters::class,
    StatisticsTypeConverters::class])
@Database(
    entities = [
        BookEntityDB::class,
        RootFolderEntityDB::class,
        SettingsEntityDTO::class,
        StatisticsEntityDB::class
               ],
    version = 3,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2
        ),
        AutoMigration(
            from = 2,
            to = 3
        )
    ],
    exportSchema = true
)
abstract class AudiobooksDatabase : RoomDatabase() {
    abstract fun audiobooksDao(): AudiobooksDao

    abstract fun rootFoldersDao(): RootFoldersDao

    abstract fun timeUpdateDao(): TimeUpdateDao

    abstract fun settingsDao(): SettingsDao

    abstract fun statisticsDao(): StatisticsDao
}