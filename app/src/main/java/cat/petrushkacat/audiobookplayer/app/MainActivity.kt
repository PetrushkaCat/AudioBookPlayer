package cat.petrushkacat.audiobookplayer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import cat.petrushkacat.audiobookplayer.app.ui.MainComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.theme.AudioBookPlayerTheme
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.RootComponent
import cat.petrushkacat.audiobookplayer.core.components.main.MainComponentImpl
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.core.models.Theme
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import cat.petrushkacat.audiobookplayer.core.repository.SettingsRepository
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

    private val isDarkTheme = MutableStateFlow(true)

    private var job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = MainComponentImpl(
            defaultComponentContext(),
            this,
            rootFoldersRepository,
            audiobooksRepository,
            audiobookServiceHandler,
            settingsRepository,
            sensorListener
        )

        CoroutineScope(job + Dispatchers.Default).launch {
            if (settingsRepository.getSettings().first() == null) { //nope it's not always false
                settingsRepository.saveSettings(SettingsEntity())
            }
                settingsRepository.getSettings().takeWhile { job.isActive }
                    .collect {
                        isDarkTheme.value = it.theme == Theme.DARK
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
                    MainComponentUi(component = root)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AudioBookPlayerTheme {
        Greeting("Android")
    }
}

@Composable
fun A(com: RootComponent) {
    val a by com.models.collectAsState()

    Text(a.a)
}