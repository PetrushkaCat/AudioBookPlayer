package cat.petrushkacat.audiobookplayer.domain.usecases

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
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetAllStatisticsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.GetStatisticsDetailsUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.SaveStatisticsUseCase

data class UseCasesProvider(
    val booksUseCases: BooksUseCases,
    val foldersUseCases: FoldersUseCases,
    val settingsUseCases: SettingsUseCases,
    val statisticsUseCases: StatisticsUseCases
)

data class BooksUseCases(
    val deleteAllBooksInFolderUseCase: DeleteAllBooksInFolderUseCase,
    val deleteBookUseCase: DeleteBookUseCase,
    val deleteIfNoInListUseCase: DeleteIfNoInListUseCase,
    val getBooksUseCase: GetBooksUseCase,
    val getBookUseCase: GetBookUseCase,
    val getSearchedBooksUseCase: GetSearchedBooksUseCase,
    val saveBookUseCase: SaveBookUseCase,
    val updateBookNotesUseCase: UpdateBookNotesUseCase,
    val updateBookUseCase: UpdateBookUseCase
    )

data class FoldersUseCases(
    val addFolderUseCase: AddFolderUseCase,
    val deleteFolderUseCase: DeleteFolderUseCase,
    val getFoldersUseCase: GetFoldersUseCase,
    val updateFolderUseCase: UpdateFolderUseCase
)

data class SettingsUseCases(
    val getSettingsUseCase: GetSettingsUseCase,
    val saveSettingsUseCase: SaveSettingsUseCase
)

data class StatisticsUseCases(
    val getAllStatisticsUseCase: GetAllStatisticsUseCase,
    val getStatisticsDetailsUseCase: GetStatisticsDetailsUseCase,
    val saveStatisticsUseCase: SaveStatisticsUseCase
)