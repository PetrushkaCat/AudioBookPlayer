package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.usecases.FakeAudiobooksRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetSearchedBooksUseCaseTest {

    private lateinit var fakeRepository: FakeAudiobooksRepository

    private lateinit var useCase: GetSearchedBooksUseCase

    @Before
    fun before() {
        fakeRepository = FakeAudiobooksRepository()
        useCase = GetSearchedBooksUseCase(GetBooksUseCase(fakeRepository))
    }

    @Test
    fun `returns searched books`() {
        runBlocking {
            val list = useCase.invoke("1", null).first()
            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB.filter {
                it.name == "1"
            })
        }
    }

    @Test
    fun `does not return not searched books`() {
        runBlocking {
            val list = useCase.invoke("2", null).first()
            Truth.assertThat(list).containsNoneIn(fakeRepository.fakeListDB.filter {
                it.name != "2"
            })
        }
    }

    @Test
    fun `returns all if not searching and folder is null`() {
        runBlocking {
            val list = useCase.invoke("", null).first()
            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB)
        }
    }

    @Test
    fun `returns folder if not searching and folder is not null`() {
        runBlocking {
            val list = useCase.invoke("", "first").first()
            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB.filter {
                it.rootFolderUri == "first"
            })
        }
    }

    @Test
    fun `does not return books not in the folder if not searching and folder is not null`() {
        runBlocking {
            val list = useCase.invoke("", "first").first()
            Truth.assertThat(list).containsNoneIn(fakeRepository.fakeListDB.filter {
                it.rootFolderUri != "first"
            })
        }
    }

}