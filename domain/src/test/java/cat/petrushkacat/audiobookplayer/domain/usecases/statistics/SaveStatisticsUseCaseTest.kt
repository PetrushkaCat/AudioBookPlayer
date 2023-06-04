package cat.petrushkacat.audiobookplayer.domain.usecases.statistics

import cat.petrushkacat.audiobookplayer.domain.models.ListenedInterval
import cat.petrushkacat.audiobookplayer.domain.usecases.FakeStatisticsRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SaveStatisticsUseCaseTest {

    lateinit var repository: FakeStatisticsRepository

    lateinit var useCase: SaveStatisticsUseCase

    @Before
    fun before() {
        repository = FakeStatisticsRepository()

        useCase = SaveStatisticsUseCase(repository)
    }
    // max day time: 86340000
    @Test
    fun `save within day that contains statistics already`() {
        runBlocking {
            useCase.invoke(
                ListenedInterval(86302000, 86303000, "book2"),
                2007, 7, 3,
                2007, 7, 2)

            Truth.assertThat(repository.fakeStatisticsDB).hasSize(2)
            Truth.assertThat(repository.fakeStatisticsDB[1].listenedIntervals.intervals).hasSize(2)
            Truth.assertThat(repository.fakeStatisticsDB[1].listenedTime).isEqualTo(2000)
        }
    }

    @Test
    fun `save when interval contains two days and prev day contains statistics`() {
        runBlocking {
            useCase.invoke(
                ListenedInterval(86399000, 1000, "book2"),
                2007, 7, 4,
                2007, 7, 3)

            Truth.assertThat(repository.fakeStatisticsDB).hasSize(3)
            Truth.assertThat(repository.fakeStatisticsDB[1].listenedIntervals.intervals).hasSize(2)
            Truth.assertThat(repository.fakeStatisticsDB[2].listenedIntervals.intervals).hasSize(1)
            Truth.assertThat(repository.fakeStatisticsDB[1].listenedTime).isEqualTo(1999)
            Truth.assertThat(repository.fakeStatisticsDB[2].listenedTime).isEqualTo(1000)
        }
    }
}