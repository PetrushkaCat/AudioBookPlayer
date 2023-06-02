package cat.petrushkacat.audiobookplayer.app.di

import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.domain.usecases.BooksUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.FoldersUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.SettingsUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteAllBooksInFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteIfNoInListUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetSearchedBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.SaveBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookNotesUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.AddFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.DeleteFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.UpdateFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.SaveSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideUseCasesProvider(
        audiobooksRepository: AudiobooksRepository,
        rootFoldersRepository: RootFoldersRepository,
        settingsRepository: SettingsRepository
    ): UseCasesProvider {
        val getBooksUseCase = GetBooksUseCase(audiobooksRepository)
        return UseCasesProvider(
            booksUseCases = BooksUseCases(
                deleteAllBooksInFolderUseCase = DeleteAllBooksInFolderUseCase(audiobooksRepository),
                deleteBookUseCase = DeleteBookUseCase(audiobooksRepository),
                deleteIfNoInListUseCase = DeleteIfNoInListUseCase(audiobooksRepository),
                getBooksUseCase = getBooksUseCase,
                getBookUseCase = GetBookUseCase(audiobooksRepository),
                getSearchedBooksUseCase = GetSearchedBooksUseCase(getBooksUseCase),
                saveBookUseCase = SaveBookUseCase(audiobooksRepository),
                updateBookNotesUseCase = UpdateBookNotesUseCase(audiobooksRepository),
                updateBookUseCase = UpdateBookUseCase(audiobooksRepository)
            ),
            foldersUseCases = FoldersUseCases(
                addFolderUseCase = AddFolderUseCase(rootFoldersRepository),
                deleteFolderUseCase = DeleteFolderUseCase(rootFoldersRepository),
                getFoldersUseCase = GetFoldersUseCase(rootFoldersRepository),
                updateFolderUseCase = UpdateFolderUseCase(rootFoldersRepository)
            ),
            settingsUseCases = SettingsUseCases(
                getSettingsUseCase = GetSettingsUseCase(settingsRepository),
                saveSettingsUseCase = SaveSettingsUseCase(settingsRepository)
            )
        )
    }
}