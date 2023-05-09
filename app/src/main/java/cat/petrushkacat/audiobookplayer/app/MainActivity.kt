package cat.petrushkacat.audiobookplayer.app

import android.hardware.Sensor
import android.hardware.SensorManager
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
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import cat.petrushkacat.audiobookplayer.app.ui.MainComponentUi
import cat.petrushkacat.audiobookplayer.core.components.RootComponent
import cat.petrushkacat.audiobookplayer.app.ui.theme.AudioBookPlayerTheme
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.core.components.main.MainComponentImpl
import cat.petrushkacat.audiobookplayer.core.repository.AudiobooksRepository
import cat.petrushkacat.audiobookplayer.core.repository.RootFoldersRepository
import com.arkivanov.decompose.defaultComponentContext
import dagger.hilt.android.AndroidEntryPoint
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
    lateinit var sensorListener: SensorListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = MainComponentImpl(
            defaultComponentContext(),
            this,
            rootFoldersRepository,
            audiobooksRepository,
            audiobookServiceHandler,
            sensorListener
        )

        /*val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        try {
            sensorManager.registerListener(
                sensorListener,
                sensor!!,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("MainActivity onCreate()", "no sensor Registered")
        }*/

        setContent {
            AudioBookPlayerTheme {
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