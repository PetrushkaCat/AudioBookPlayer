package cat.petrushkacat.audiobookplayer.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import cat.petrushkacat.audiobookplayer.app.ui.BooksListComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.BookshelfComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.MainComponentUi
import cat.petrushkacat.audiobookplayer.core.components.RootComponent
import cat.petrushkacat.audiobookplayer.app.ui.theme.AudioBookPlayerTheme
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookMediaService
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.di.AudiobookPlayerModule
import cat.petrushkacat.audiobookplayer.core.components.main.MainComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponentImpl
import cat.petrushkacat.audiobookplayer.data.repository.AudiobooksRepositoryImpl
import cat.petrushkacat.audiobookplayer.data.repository.RootFoldersRepositoryImpl
import com.arkivanov.decompose.defaultComponentContext
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var audiobookServiceHandler: AudiobookServiceHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = MainComponentImpl(
            defaultComponentContext(),
            this,
            RootFoldersRepositoryImpl(App.database.rootFoldersDao()),
            AudiobooksRepositoryImpl(App.database.audiobooksDao()),
            audiobookServiceHandler
        )

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

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, AudiobookMediaService::class.java))
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