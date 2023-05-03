package cat.petrushkacat.audiobookplayer.app.ui

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.BookshelfComponent
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent

@Composable
fun BooksListComponentUi(component: BooksListComponent) {

val model by component.models.collectAsState()

    Log.d("folder7", model.toString())
    LazyColumn() {
        items(model.size) {
            BookItem(model = model[it], Modifier.clickable {
                component.onBookClick(Uri.parse(model[it].folderUri))
            })
        }
    }
}


@Composable
fun BookItem(model: BooksListComponent.Model, modifier: Modifier) {
    Column(modifier = modifier.wrapContentSize()) {
        Text(model.name)
        Text("+++")
        //Text(model.folderUri.toString())
        Text("+++")
        //Text(model.image.toString())
        Text("---------")
    }
}