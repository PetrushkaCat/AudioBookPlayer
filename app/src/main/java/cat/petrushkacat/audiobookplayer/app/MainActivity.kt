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
import cat.petrushkacat.audiobookplayer.app.ui.BooksListComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.BookshelfComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.MainComponentUi
import cat.petrushkacat.audiobookplayer.core.components.RootComponent
import cat.petrushkacat.audiobookplayer.app.ui.theme.AudioBookPlayerTheme
import cat.petrushkacat.audiobookplayer.core.components.main.MainComponentImpl
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponentImpl
import com.arkivanov.decompose.defaultComponentContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = MainComponentImpl(defaultComponentContext(), this)
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