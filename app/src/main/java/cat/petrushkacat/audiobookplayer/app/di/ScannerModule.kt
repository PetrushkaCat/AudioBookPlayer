package cat.petrushkacat.audiobookplayer.app.di

import android.content.Context
import cat.petrushkacat.audiobookplayer.app.scanner.Scanner
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
class ScannerModule {

    @Provides
    fun provideScanner(
        @ActivityContext context: Context,
        useCasesProvider: UseCasesProvider
    ) = Scanner(
        context,
        useCasesProvider.foldersUseCases.getFoldersUseCase,
        useCasesProvider.booksUseCases.saveBookUseCase,
        useCasesProvider.booksUseCases.getBooksUrisUseCase,
        useCasesProvider.booksUseCases.deleteBookUseCase
    )
}