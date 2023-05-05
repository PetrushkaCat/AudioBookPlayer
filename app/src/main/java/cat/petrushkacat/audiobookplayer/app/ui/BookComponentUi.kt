package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent

@Composable
fun BookComponentUi(component: BookComponent) {

    val model by component.models.collectAsState()

    Column {
        Text(model.name, modifier = Modifier.clickable {
            component.onButtonPlayPauseClick()
        })
        //Text(model.folderUri.toString())
        Text("---")

        LazyColumn {
            items(model.chapters.chapters.size) {
                Text(model.chapters.chapters[it].name)
                //Text(model.chapters[it].uri.toString())
                Text("+++")
            }
        }

    }
}