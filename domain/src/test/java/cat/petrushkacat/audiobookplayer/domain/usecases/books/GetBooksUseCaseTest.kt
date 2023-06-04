package cat.petrushkacat.audiobookplayer.domain.usecases.books

import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.FakeAudiobooksRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetBooksUseCaseTest {

    private lateinit var fakeRepository: FakeAudiobooksRepository

    private lateinit var useCase: GetBooksUseCase

    @Before
    fun before() {
        fakeRepository = FakeAudiobooksRepository()
        useCase = GetBooksUseCase(fakeRepository)
    }

    @Test
    fun `get all returns all books`() {
        runBlocking {
            val list = useCase.invoke(GetBooksUseCase.BooksType.All).first()
            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB)
        }
    }

    @Test
    fun `get folder returns folder books`() {
        runBlocking {
            val list = useCase.invoke(GetBooksUseCase.BooksType.Folder("true")).first()
            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB.filter {
                it.rootFolderUri == "true"
            })
        }
    }

    @Test
    fun `get folder does not return other folders books`() {
        runBlocking {
            val list = useCase.invoke(GetBooksUseCase.BooksType.Folder("true")).first()
            Truth.assertThat(list).containsNoneIn(fakeRepository.fakeListDB.filter {
                it.rootFolderUri != "true"
            })
        }
    }

    @Test
    fun `get completed returns completed`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.Completed).first()

            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB.filter {
                it.isCompleted
            })
        }
    }

    @Test
    fun `get completed does not return not completed`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.Completed).first()

            Truth.assertThat(list).containsNoneIn(fakeRepository.fakeListDB.filter {
                !it.isCompleted
            })
        }
    }

    @Test
    fun `get favorite returns favorite`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.Favorites).first()

            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB.filter {
                it.isFavorite
            })
        }
    }

    @Test
    fun `get favorite does not return not favorite`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.Favorites).first()

            Truth.assertThat(list).containsNoneIn(fakeRepository.fakeListDB.filter {
                !it.isFavorite
            })
        }
    }

    @Test
    fun `get listen later returns listen later`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.ListenLater).first()

            Truth.assertThat(list).containsExactlyElementsIn(fakeRepository.fakeListDB.filter {
                it.isWantToListen
            })
        }
    }

    @Test
    fun `get listen later does not return not listen later`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.ListenLater).first()

            Truth.assertThat(list).containsNoneIn(fakeRepository.fakeListDB.filter {
                !it.isWantToListen
            })
        }
    }

    @Test
    fun `get all returns sorted books`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.All).first()

            Truth.assertThat(list).containsExactlyElementsIn(sortBooks(fakeRepository.fakeListDB)).inOrder()
        }
    }

    @Test
    fun `get folder returns sorted books`() {
        runBlocking {
            val list: List<BookListEntity> =
                useCase.invoke(GetBooksUseCase.BooksType.Folder("false")).first()

            Truth.assertThat(list).containsExactlyElementsIn(sortBooks(fakeRepository.fakeListDB.filter {
                it.rootFolderUri == "false"
            })).inOrder()
        }
    }


    private fun sortBooks(books: List<BookListEntity>): List<BookListEntity> {
        val started: MutableList<BookListEntity> = mutableListOf()
        val completed: MutableList<BookListEntity> = mutableListOf()
        val notStarted: MutableList<BookListEntity> = mutableListOf()
        books.forEach { book ->
            if (book.isStarted && !book.isCompleted) {
                started.add(book)
            } else if (book.isCompleted) {
                completed.add(book)
            } else {
                notStarted.add(book)
            }
        }
        started.sortByDescending { startedBook ->
            startedBook.lastTimeListened
        }

        started += notStarted + completed
        return started.toMutableList()
    }
}