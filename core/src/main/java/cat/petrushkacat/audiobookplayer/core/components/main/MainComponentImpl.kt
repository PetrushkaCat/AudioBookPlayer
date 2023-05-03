package cat.petrushkacat.audiobookplayer.core.components.main

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.BookPlayerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.util.toStateFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

class MainComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val rootFoldersRepository: RootFoldersRepository,
    private val audiobooksRepository: AudiobooksRepository
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
    ): MainComponent.Child = when(config) {
        is ChildConfig.Bookshelf -> {
            MainComponent.Child.Bookshelf(BookshelfComponentImpl(componentContext, context, rootFoldersRepository, audiobooksRepository, {
                navigation.push(ChildConfig.BookPlayer(it))
            }))
        }
        is ChildConfig.BookPlayer -> {
            MainComponent.Child.BookPlayer(BookPlayerComponentImpl(componentContext, context, audiobooksRepository, config.bookUri))
        }
    }

    private sealed interface ChildConfig: Parcelable {
        @Parcelize
        object Bookshelf: ChildConfig

        @Parcelize
        data class BookPlayer(val bookUri: Uri) : ChildConfig
    }
}