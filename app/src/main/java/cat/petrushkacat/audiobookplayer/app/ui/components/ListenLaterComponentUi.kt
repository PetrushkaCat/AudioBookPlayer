package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.BookDropdownMenuComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.BookGridItem
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.BookListItem
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.CommonTopAppBar
import cat.petrushkacat.audiobookplayer.components.components.main.listenlater.ListenLaterComponent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListenLaterComponentUi(component: ListenLaterComponent) {
    val model by component.models.collectAsState()
    val settings by component.settings.collectAsState()

    val isDropdownMenuExpanded = rememberSaveable { mutableStateOf(false) }
    val selectedBook = component.bookDropDownMenuComponent.selectedBook.collectAsState()

    Column {
        CommonTopAppBar(
            title = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.listen_later),
            onBack = {
                component.onBack()
            }
        )
        if (settings.grid == cat.petrushkacat.audiobookplayer.domain.models.Grid.SMALL_CELLS || settings.grid == cat.petrushkacat.audiobookplayer.domain.models.Grid.BIG_CELLS) {
            val size = if (settings.grid == cat.petrushkacat.audiobookplayer.domain.models.Grid.BIG_CELLS) 150.dp else 100.dp
            LazyVerticalGrid(
                columns = GridCells.Adaptive(size),
                contentPadding = PaddingValues(4.dp),
                state = rememberLazyGridState(0)
            ) {
                items(model.size) {
                    Box {
                        if (isDropdownMenuExpanded.value &&
                            selectedBook.value.folderUri == model[it].folderUri
                        ) {
                            BookDropdownMenuComponentUi(
                                component = component.bookDropDownMenuComponent,
                                expanded = isDropdownMenuExpanded,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        BookGridItem(model = model[it], Modifier.combinedClickable(
                            onClick = {
                                component.onBookClick(model[it].folderUri)
                            },
                            onLongClick = {
                                component.bookDropDownMenuComponent.selectBook(model[it])
                                isDropdownMenuExpanded.value = true
                            }
                        ))
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                state = rememberLazyListState()
            ) {
                items(model.size) {
                    Box {
                        if (isDropdownMenuExpanded.value &&
                            selectedBook.value.folderUri == model[it].folderUri
                        ) {
                            BookDropdownMenuComponentUi(
                                component = component.bookDropDownMenuComponent,
                                expanded = isDropdownMenuExpanded,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        BookListItem(model = model[it], Modifier.combinedClickable(
                            onClick = {
                                component.onBookClick(model[it].folderUri)
                            },
                            onLongClick = {
                                component.bookDropDownMenuComponent.selectBook(model[it])
                                isDropdownMenuExpanded.value = true
                            }
                        ))
                    }
                }
            }
        }

        if (model.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.empty), textAlign = TextAlign.Center)
            }
        }
    }
}