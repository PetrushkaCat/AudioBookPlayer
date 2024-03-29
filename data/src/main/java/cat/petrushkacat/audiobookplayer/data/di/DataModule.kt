package cat.petrushkacat.audiobookplayer.data.di

import android.content.Context
import androidx.room.Room
import cat.petrushkacat.audiobookplayer.audioservice.repository.TimeUpdateRepository
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDatabase
import cat.petrushkacat.audiobookplayer.data.db.dao.AudiobooksDao
import cat.petrushkacat.audiobookplayer.data.db.dao.RootFoldersDao
import cat.petrushkacat.audiobookplayer.data.db.dao.SettingsDao
import cat.petrushkacat.audiobookplayer.data.db.dao.StatisticsDao
import cat.petrushkacat.audiobookplayer.data.db.dao.TimeUpdateDao
import cat.petrushkacat.audiobookplayer.data.repository.AudiobooksRepositoryImpl
import cat.petrushkacat.audiobookplayer.data.repository.RootFoldersRepositoryImpl
import cat.petrushkacat.audiobookplayer.data.repository.SettingsRepositoryImpl
import cat.petrushkacat.audiobookplayer.data.repository.StatisticsRepositoryImpl
import cat.petrushkacat.audiobookplayer.data.repository.TimeUpdateRepositoryImpl
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAudiobookDatabase(
        @ApplicationContext context: Context
    ): AudiobooksDatabase {
        return Room.databaseBuilder(
            context,
            AudiobooksDatabase::class.java,
            "audiobooks-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAudiobooksDao(db: AudiobooksDatabase) = db.audiobooksDao()

    @Provides
    @Singleton
    fun provideRootFoldersDao(db: AudiobooksDatabase) = db.rootFoldersDao()

    @Provides
    @Singleton
    fun provideAudiobooksRepository(audiobooksDao: AudiobooksDao): AudiobooksRepository = AudiobooksRepositoryImpl(audiobooksDao)

    @Provides
    @Singleton
    fun provideRootFoldersRepository(rootFoldersDao: RootFoldersDao): RootFoldersRepository = RootFoldersRepositoryImpl(rootFoldersDao)

    @Provides
    @Singleton
    fun TimeUpdateDao(db: AudiobooksDatabase) = db.timeUpdateDao()

    @Provides
    @Singleton
    fun provideTimeUpdateRepository(timeUpdateDao: TimeUpdateDao): TimeUpdateRepository = TimeUpdateRepositoryImpl(timeUpdateDao)

    @Provides
    @Singleton
    fun provideSettingsDao(db: AudiobooksDatabase) = db.settingsDao()

    @Provides
    @Singleton
    fun provideSettingsRepository(settingsDao: SettingsDao): SettingsRepository = SettingsRepositoryImpl(settingsDao)

    @Provides
    @Singleton
    fun provideStatisticsDao(db: AudiobooksDatabase) = db.statisticsDao()

    @Provides
    @Singleton
    fun provideStatisticsRepository(statisticsDao: StatisticsDao): StatisticsRepository = StatisticsRepositoryImpl(statisticsDao)
}