package cat.petrushkacat.audiobookplayer.core.components.main.bookplayer

import android.content.Context
import android.net.Uri
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.PlayerEvent
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.main.MainComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookPlayerContainerComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookPlayerContainerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.bookplayer.BookPlayerComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.notes.NotesComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.util.componentCoroutineScopeMain
import cat.petrushkacat.audiobookplayer.core.util.toStateFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val sensorListener: SensorListener,
    private val bookUri: Uri,
    private val onBack: () -> Unit
) : BookComponent, ComponentContext by componentContext {

    private val scopeMain = componentCoroutineScopeMain()

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, BookComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = ChildConfig.BookPlayerContainer,
        handleBackButton = true,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ): BookComponent.Child = when(config) {
        is ChildConfig.BookPlayerContainer -> {
            BookComponent.Child.BookPlayerContainer(
                BookPlayerContainerComponentImpl(
                    componentContext= componentContext,
                    context = context,
                    audiobooksRepository = audiobooksRepository,
                    audiobookServiceHandler = audiobookServiceHandler,
                    sensorListener = sensorListener,
                    bookUri = bookUri,
                    onBack = onBack,
                    {
                        scopeMain.launch {
                            audiobookServiceHandler.pause()
                        }
                        navigation.push(ChildConfig.Notes)
                    }
                )
            )
        }
        is ChildConfig.Notes -> {
            BookComponent.Child.Notes(
                NotesComponentImpl(
                    componentContext,
                    audiobooksRepository,
                    audiobookServiceHandler,
                    bookUri,
                    { id, time ->
                        navigation.pop()
                        audiobookServiceHandler.setTimings(id, time)
                    }

                )
            )
        }
    }



    private sealed interface ChildConfig: Parcelable {

        @Parcelize
        object BookPlayerContainer: ChildConfig

        @Parcelize
        object Notes: ChildConfig
    }

}