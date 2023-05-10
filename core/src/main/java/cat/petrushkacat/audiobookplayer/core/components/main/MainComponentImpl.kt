package cat.petrushkacat.audiobookplayer.core.components.main

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.folderselector.FoldersComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.settings.SettingsComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.toStateFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

class MainComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val rootFoldersRepository: RootFoldersRepository,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val settingsRepository: SettingsRepository,
    private val sensorListener: SensorListener
) : MainComponent, ComponentContext by componentContext {


    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, MainComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.Bookshelf,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): MainComponent.Child = when (config) {
        is ChildConfig.Bookshelf -> {
            MainComponent.Child.Bookshelf(
                BookshelfComponentImpl(componentContext, context, rootFoldersRepository, audiobooksRepository, settingsRepository,
                    {
                        navigation.push(ChildConfig.Book(it))
                    },
                    {
                        navigation.push(ChildConfig.Folders)
                    },
                    {
                        navigation.push(ChildConfig.Settings)
                    }
                ))
        }

        is ChildConfig.Book -> {
            MainComponent.Child.Book(
                BookComponentImpl(componentContext, context,
                    audiobooksRepository, audiobookServiceHandler, sensorListener, config.bookUri, {
                        navigation.pop()
                        BookPlayerComponentImpl.isInitialized = false
                    })
            )
        }

        is ChildConfig.Folders -> {
            MainComponent.Child.Folder(
                FoldersComponentImpl(componentContext, context, audiobooksRepository, rootFoldersRepository, {  }, {  })
            )
        }

        is ChildConfig.Settings -> {
            MainComponent.Child.Settings(
                SettingsComponentImpl(componentContext, settingsRepository)
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
    }
}