package cat.petrushkacat.audiobookplayer.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDatabase
import cat.petrushkacat.audiobookplayer.data.db.dao.RootFoldersDao
import cat.petrushkacat.audiobookplayer.domain.models.RootFolderEntity
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RootFoldersRepositoryImplTest {

    private lateinit var db: AudiobooksDatabase
    private lateinit var dao: RootFoldersDao
    private lateinit var repository: RootFoldersRepositoryImpl

    @Before
    fun before() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AudiobooksDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.rootFoldersDao()
        repository = RootFoldersRepositoryImpl(dao)
    }

    @After
    fun after() {
        db.close()
    }

    @Test
    fun `returns empty list if there is no folders`() = runTest {
        val folders = repository.getFolders().first()
        Truth.assertThat(folders).isEmpty()
    }

    @Test
    fun `returns right folders`() = runTest {
        val folder1 = RootFolderEntity("1", "1", false)
        val folder2 = RootFolderEntity("2", "2", true)
        repository.addFolder(folder1)
        repository.addFolder(folder2)

        val folders = repository.getFolders().first()
        Truth.assertThat(folders).containsExactly(folder1, folder2)
    }

    @Test
    fun `delete folder works well`() = runTest {
        val folder1 = RootFolderEntity("1", "1", false)
        val folder2 = RootFolderEntity("2", "2", true)
        repository.addFolder(folder1)
        repository.addFolder(folder2)
        repository.deleteFolder(folder1)
        val folders = repository.getFolders().first()
        Truth.assertThat(folders).containsExactly(folder2)
    }

    @Test
    fun `update folder works well`() = runTest {
        val folder1 = RootFolderEntity("1", "1", false)
        val folder2 = RootFolderEntity("2", "2", true)
        repository.addFolder(folder1)
        repository.addFolder(folder2)
        repository.updateFolder(folder1.copy(isCurrent = true))

        val folders = repository.getFolders().first()
        Truth.assertThat(folders).containsExactly(folder1.copy(isCurrent = true), folder2)
    }
}