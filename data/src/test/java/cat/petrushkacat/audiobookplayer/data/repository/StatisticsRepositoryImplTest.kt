package cat.petrushkacat.audiobookplayer.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDatabase
import cat.petrushkacat.audiobookplayer.data.db.dao.StatisticsDao
import cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval
import cat.petrushkacat.audiobookplayer.domain.models.ListenedIntervals
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StatisticsRepositoryImplTest {

    private lateinit var db: AudiobooksDatabase
    private lateinit var dao: StatisticsDao
    private lateinit var repository: StatisticsRepositoryImpl

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AudiobooksDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.statisticsDao()
        repository = StatisticsRepositoryImpl(dao)
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun `returns null if there is not statistics that day`() = runTest {
        val details = repository.getDetails(1,1,1).first()
        Truth.assertThat(details).isNull()
    }

    @Test
    fun `returns empty list if there is no statistics`() = runTest {
        val details = repository.getAll().first()
        Truth.assertThat(details).isEmpty()
    }

    @Test
    fun `returns right list if there is statistics`() = runTest {
        val statistics1 = StatisticsEntity(1,1,1,1, ListenedIntervals(emptyList()))
        val statistics2 = StatisticsEntity(1,1,2,1000, ListenedIntervals(listOf(
            ListenedInterval(0, 1000, "book")
        )))
        repository.save(statistics1)
        repository.save(statistics2)

        val details = repository.getAll().first()
        Truth.assertThat(details).containsExactly(statistics1,
            statistics2.copy(listenedIntervals = ListenedIntervals(emptyList()))).inOrder()
    }
}