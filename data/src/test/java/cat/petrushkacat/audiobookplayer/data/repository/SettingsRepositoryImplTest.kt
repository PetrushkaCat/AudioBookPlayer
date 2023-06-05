package cat.petrushkacat.audiobookplayer.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import cat.petrushkacat.audiobookplayer.audioservice.model.AudioServiceSettings
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDatabase
import cat.petrushkacat.audiobookplayer.data.db.dao.SettingsDao
import cat.petrushkacat.audiobookplayer.domain.models.Grid
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.models.Theme
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsRepositoryImplTest {

    private lateinit var db: AudiobooksDatabase
    private lateinit var dao: SettingsDao
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AudiobooksDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.settingsDao()
        repository = SettingsRepositoryImpl(dao)
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun `returns not null settings if there is no settings yet`() = runTest {
        val settings = repository.getSettings().first()
        Truth.assertThat(settings).isNotNull()
    }

    @Test
    fun `returns not null audio service settings if there is no settings yet`() = runTest {
        val settings = repository.getAudioServiceSettings().first()
        Truth.assertThat(settings).isNotNull()
    }

    @Test
    fun `returns right audio service settings if there is settings`() = runTest {
        val settingsSaved = SettingsEntity(1, 1, 1,
            1, 1, Theme.DARK, Grid.SMALL_CELLS, 1,
            SettingsEntity.SleepTimerType.EndOfTheChapter)

        repository.saveSettings(settingsSaved)

        val settings = repository.getAudioServiceSettings().first()
        Truth.assertThat(settings).isEqualTo(AudioServiceSettings(1,1,1,1,1,1))
    }

    @Test
    fun `returns right settings if there is settings`() = runTest {
        val settingsSaved = SettingsEntity(1, 1, 1,
            1, 1, Theme.DARK, Grid.SMALL_CELLS, 1,
            SettingsEntity.SleepTimerType.EndOfTheChapter)

        repository.saveSettings(settingsSaved)

        val settings = repository.getSettings().first()
        Truth.assertThat(settings).isEqualTo(settingsSaved)
    }
}