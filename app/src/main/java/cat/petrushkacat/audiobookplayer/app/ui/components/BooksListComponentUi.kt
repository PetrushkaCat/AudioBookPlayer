package cat.petrushkacat.audiobookplayer.app.ui.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.BookDropdownMenuComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.BookGridItem
import cat.petrushkacat.audiobookplayer.app.ui.components.shared.BookListItem
import cat.petrushkacat.audiobookplayer.app.ui.theme.Purple40
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.bookslist.BooksListComponent

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun BooksListComponentUi(component: BooksListComponent) {

    val model by component.models.collectAsState()
    val settings by component.settings.collectAsState()
    val isRefreshing by component.isRefreshing.collectAsState()
    val foldersToProcess by component.foldersToProcess.collectAsState()
    val foldersProcessed by component.foldersProcessed.collectAsState()

    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        component.refresh()
    })

    val isDropdownMenuExpanded = rememberSaveable { mutableStateOf(false) }
    val selectedBook = component.bookDropDownMenuComponent.selectedBook.collectAsState()
    val isSearching by component.isSearching.collectAsState()

    val strokeWidth = 5.dp
    val circleColor = Purple40

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        if(!isRefreshing) {
            PullRefreshIndicator(
                isRefreshing,
                pullRefreshState,
                Modifier
                    .align(Alignment.TopCenter)
                    .clickable { }
            )
        }
        Column {
            if (foldersToProcess != 0 && foldersProcessed < foldersToProcess) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.drawBehind {
                            drawCircle(
                                circleColor,
                                radius = size.width / 2 - strokeWidth.toPx() / 2,
                                style = Stroke(strokeWidth.toPx())
                            )
                        },
                        color = Color.LightGray,
                        strokeWidth = strokeWidth
                    )
                    Row {
                        Text(stringResource(id = R.string.folders_processed) + " " + foldersProcessed.toString() + " ")
                        Text(stringResource(id = R.string.of) + " " + foldersToProcess.toString())
                    }
                }
            }
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
                                    component.onBookClick(Uri.parse(model[it].folderUri))
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
                                    component.onBookClick(Uri.parse(model[it].folderUri))
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

            if(model.isEmpty() && !isSearching) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(id = R.string.no_books_text), textAlign = TextAlign.Center)
                }
            } else if(model.isEmpty() && isSearching) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(stringResource(id = R.string.nothing_found), textAlign = TextAlign.Center)
                    }
                }
        }
    }
}