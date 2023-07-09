package cat.petrushkacat.audiobookplayer.components.components.main

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.BookComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.bookplayer.book.bookplayer.BookPlayerComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.BookshelfComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.folderselector.FoldersComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists.completedbooks.CompletedBooksComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists.favorites.FavoritesComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.marked_books_lists.listenlater.ListenLaterComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.settings.SettingsComponentImpl
import cat.petrushkacat.audiobookplayer.components.components.main.statistics.StatisticsComponentImpl
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeIO
import cat.petrushkacat.audiobookplayer.components.util.toStateFlow
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val useCasesProvider: UseCasesProvider,
    private val audiobookServiceHandler: AudiobookServiceHandler
) : MainComponent, ComponentContext by componentContext {

    private val scopeIO = componentCoroutineScopeIO()

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, MainComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Bookshelf,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    init {
        Log.d("init", "main")
    }

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        is ChildConfig.Bookshelf -> {
            MainComponent.Child.Bookshelf(
                BookshelfComponentImpl(componentContext, context, useCasesProvider,
                   onBookSelect =  {
                        val file = DocumentFile.fromSingleUri(context, it)
                        if(file!!.exists()) {
                            navigation.push(ChildConfig.Book(it))
                        } else {
                            Toast.makeText(context,
                                "Seems like there is no more such a book. Deleting...",
                                Toast.LENGTH_SHORT).show()
                            scopeIO.launch {
                                useCasesProvider.booksUseCases.deleteBookUseCase(it.toString())
                            }
                        }
                    },
                    onFolderButtonClick = {
                        navigation.push(ChildConfig.Folders)
                    },
                    onSettingsClicked = {
                        navigation.push(ChildConfig.Settings)
                    },
                    onFavoritesClicked = {
                        navigation.push(ChildConfig.Favorites)
                    },
                    onListenLaterClicked = {
                        navigation.push(ChildConfig.ListenLater)
                    },
                    onCompletedBooksClicked = {
                        navigation.push(ChildConfig.CompletedBooks)
                    },
                    onStatisticsClicked = {
                        navigation.push(ChildConfig.Statistics)
                    }
                ))
        }

        is ChildConfig.Book -> {
            MainComponent.Child.Book(
                BookComponentImpl(componentContext, context,
                    useCasesProvider,
                    audiobookServiceHandler, config.bookUri,
                    onBack = {
                        navigation.pop()
                        BookPlayerComponentImpl.isInitialized = false
                    }
                )
            )
        }

        is ChildConfig.Folders -> {
            MainComponent.Child.Folder(
                FoldersComponentImpl(componentContext, context,
                    useCasesProvider.foldersUseCases.getFoldersUseCase,
                    useCasesProvider.foldersUseCases.deleteFolderUseCase,
                    useCasesProvider.foldersUseCases.addFolderUseCase,
                    useCasesProvider.booksUseCases.deleteAllBooksInFolderUseCase,
                    useCasesProvider.booksUseCases.saveBookUseCase,
                    onBackClicked = {
                        navigation.pop()
                    }
                )
            )
        }

        is ChildConfig.Settings -> {
            MainComponent.Child.Settings(
                SettingsComponentImpl(
                    componentContext,
                    useCasesProvider.settingsUseCases.saveSettingsUseCase,
                    useCasesProvider.settingsUseCases.getSettingsUseCase,
                    onBackClicked = {
                        navigation.pop()
                    }
                )
            )
        }

        ChildConfig.Favorites -> {
            MainComponent.Child.Favorites(
                FavoritesComponentImpl(
                    componentContext = componentContext,
                    getBookUseCase = useCasesProvider.booksUseCases.getBookUseCase,
                    updateBookUseCase = useCasesProvider.booksUseCases.updateBookUseCase,
                    getBooksUseCase = useCasesProvider.booksUseCases.getBooksUseCase,
                    getSettingsUseCase = useCasesProvider.settingsUseCases.getSettingsUseCase,
                    onBackPressed = {
                        navigation.pop()
                    },
                    onBookClicked = {
                        navigation.push(ChildConfig.Book(Uri.parse(it)))
                    }
                )
            )
        }
        ChildConfig.ListenLater -> {
            MainComponent.Child.ListenLater(
                ListenLaterComponentImpl(
                    componentContext = componentContext,
                    getBookUseCase = useCasesProvider.booksUseCases.getBookUseCase,
                    updateBookUseCase = useCasesProvider.booksUseCases.updateBookUseCase,
                    getBooksUseCase = useCasesProvider.booksUseCases.getBooksUseCase,
                    getSettingsUseCase = useCasesProvider.settingsUseCases.getSettingsUseCase,
                    onBackPressed = {
                        navigation.pop()
                    },
                    onBookClicked = {
                        navigation.push(ChildConfig.Book(Uri.parse(it)))
                    }
                )
            )
        }

        ChildConfig.CompletedBooks -> {
            MainComponent.Child.CompletedBooks(
                CompletedBooksComponentImpl(
                    componentContext = componentContext,
                    getBookUseCase = useCasesProvider.booksUseCases.getBookUseCase,
                    updateBookUseCase = useCasesProvider.booksUseCases.updateBookUseCase,
                    getBooksUseCase = useCasesProvider.booksUseCases.getBooksUseCase,
                    getSettingsUseCase = useCasesProvider.settingsUseCases.getSettingsUseCase,
                    onBackPressed = {
                        navigation.pop()
                    },
                    onBookClicked = {
                        navigation.push(ChildConfig.Book(Uri.parse(it)))
                    }
                )
            )
        }

        ChildConfig.Statistics -> {
            MainComponent.Child.Statistics(
                StatisticsComponentImpl(
                    componentContext = componentContext,
                    useCasesProvider = useCasesProvider,
                    onBackClicked = {
                        navigation.pop()
                    }
                )
            )
        }
    }

    private sealed interface ChildConfig : Parcelable {
        @Parcelize
        object Bookshelf : ChildConfig

        @Parcelize
        data class Book(val bookUri: Uri) : ChildConfig

        @Parcelize
        object Folders: ChildConfig

        @Parcelize
        object Settings: ChildConfig

        @Parcelize
        object ListenLater: ChildConfig

        @Parcelize
        object Favorites: ChildConfig

        @Parcelize
        object CompletedBooks: ChildConfig

        @Parcelize
        object Statistics: ChildConfig
    }
}