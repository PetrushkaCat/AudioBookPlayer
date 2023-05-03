package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cat.petrushkacat.audiobookplayer.core.components.main.bookplayer.book.BookComponent

@Composable
fun BookComponentUi(component: BookComponent) {

    val model by component.models.collectAsState()

    Column {
        Text(model.name)
        //Text(model.folderUri.toString())
        Text("---")

        LazyColumn {
            items(model.chapters.size) {
                Text(model.chapters[it].name)
                //Text(model.chapters[it].uri.toString())
                Text("+++")
            }
        }

    }
}