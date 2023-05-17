package cat.petrushkacat.audiobookplayer.core.components

import android.content.Context
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.main.MainComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.splashscreen.SplashScreenComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.core.util.toStateFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

class RootComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val rootFoldersRepository: RootFoldersRepository,
    private val audiobooksRepository: AudiobooksRepository,
    private val audiobookServiceHandler: AudiobookServiceHandler,
    private val settingsRepository: SettingsRepository,
    private val sensorListener: SensorListener
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<ChildConfig>()

    override val childStack: StateFlow<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialStack = {
            listOf<ChildConfig>(ChildConfig.Main, ChildConfig.Splash)
        },
        handleBackButton = false,
        childFactory = ::createChild
    ).toStateFlow(lifecycle)

    private fun createChild(
        config: ChildConfig,
        componentContext: ComponentContext
    ) = when(config) {
        is ChildConfig.Main -> {
            RootComponent.Child.Main(
                MainComponentImpl(
                    componentContext,
                    context,
                    rootFoldersRepository,
                    audiobooksRepository,
                    audiobookServiceHandler,
                    settingsRepository,
                    sensorListener
                )
            )
        }
        is ChildConfig.Splash -> {
            RootComponent.Child.Splash(
                SplashScreenComponentImpl({
                    navigation.pop()
                })
            )
        }
    }

    private sealed interface ChildConfig: Parcelable {

        @Parcelize
        object Main: ChildConfig

        @Parcelize
        object Splash: ChildConfig
    }
}