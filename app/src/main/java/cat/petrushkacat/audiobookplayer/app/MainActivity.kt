package cat.petrushkacat.audiobookplayer.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import cat.petrushkacat.audiobookplayer.app.ui.components.RootComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.theme.AudioBookPlayerTheme
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.components.components.RootComponentImpl
import cat.petrushkacat.audiobookplayer.domain.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.domain.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.domain.repository.SettingsRepository
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import com.arkivanov.decompose.defaultComponentContext
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var audiobookServiceHandler: AudiobookServiceHandler

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var audiobooksRepository: AudiobooksRepository

    @Inject
    lateinit var rootFoldersRepository: RootFoldersRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var sensorListener: SensorListener

    @Inject
    lateinit var useCasesProvider: UseCasesProvider

    private val isDarkTheme = MutableStateFlow(true)

    private var job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = RootComponentImpl(
            defaultComponentContext(),
            this,
            useCasesProvider,
            audiobookServiceHandler,
            sensorListener
        )

        Log.d("useCases", useCasesProvider.toString())

        CoroutineScope(job + Dispatchers.Default).launch {
            if (settingsRepository.getSettings().first() == null) { //nope it's not always false
                settingsRepository.saveSettings(cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity())
            }
                settingsRepository.getSettings().takeWhile { job.isActive }
                    .collect {
                        isDarkTheme.value = it.theme == cat.petrushkacat.audiobookplayer.domain.models.Theme.DARK
                     /*   while (true) {
                            Log.d("123", "${this.toString()}")
                            delay(100)
                        }*/
                    }

        }

        setContent {
            AudioBookPlayerTheme(
                darkTheme = isDarkTheme.collectAsState().value
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootComponentUi(component = root)
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}