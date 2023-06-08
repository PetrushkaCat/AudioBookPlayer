package cat.petrushkacat.audiobookplayer.app.di

import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.domain.repository.StatisticsRepository
import cat.petrushkacat.audiobookplayer.domain.usecases.BooksUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.FoldersUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.SettingsUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.StatisticsUseCases
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import cat.petrushkacat.audiobookplayer.domain.usecases.books.AddNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteAllBooksInFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteIfNoInListUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.DeleteNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUrisUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.GetSearchedBooksUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.SaveBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateBookUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.AddFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.DeleteFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.GetFoldersUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.folders.UpdateFolderUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.SaveSettingsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllMonthsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllStatisticsInMonthUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllStatisticsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetStatisticsDetailsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.SaveStatisticsUseCase
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
        settingsRepository: SettingsRepository,
        statisticsRepository: StatisticsRepository,
        saveStatisticsUseCase: SaveStatisticsUseCase,
        addNoteUseCase: AddNoteUseCase,
        updateNoteUseCase: UpdateNoteUseCase
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
                addNoteUseCase = addNoteUseCase,
                updateNoteUseCase = updateNoteUseCase,
                deleteNoteUseCase = DeleteNoteUseCase(audiobooksRepository),
                updateBookUseCase = UpdateBookUseCase(audiobooksRepository),
                getBooksUrisUseCase = GetBooksUrisUseCase(audiobooksRepository)
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
            ),
            statisticsUseCases = StatisticsUseCases(
                getAllStatisticsUseCase = GetAllStatisticsUseCase(statisticsRepository),
                getStatisticsDetailsUseCase = GetStatisticsDetailsUseCase(statisticsRepository),
                saveStatisticsUseCase = saveStatisticsUseCase,
                getAllMonthsUseCase = GetAllMonthsUseCase(statisticsRepository),
                getAllStatisticsInMonthUseCase = GetAllStatisticsInMonthUseCase(statisticsRepository)
            )
        )
    }

    @Provides
    @Singleton
    fun provideAddNoteUseCase(
        audiobooksRepository: AudiobooksRepository
    ): AddNoteUseCase {
        return AddNoteUseCase(audiobooksRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateNoteUseCase(
        audiobooksRepository: AudiobooksRepository
    ): UpdateNoteUseCase {
        return UpdateNoteUseCase(audiobooksRepository)
    }

    @Provides
    @Singleton
    fun provideSaveStatisticsUseCase(
        statisticsRepository: StatisticsRepository
    ): SaveStatisticsUseCase {
        return SaveStatisticsUseCase(statisticsRepository)
    }
}