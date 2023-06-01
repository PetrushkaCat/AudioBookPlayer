package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.BookshelfToolbarComponent
import cat.petrushkacat.audiobookplayer.domain.models.Grid
import cat.petrushkacat.audiobookplayer.core.models.RootFolderEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfToolbarComponentUi(
    component: BookshelfToolbarComponent,
    onDrawerButtonClick: () -> Unit
) {
    val settings = component.settings.collectAsState()

    val folders by component.folders.collectAsState()

    val icon = when (settings.value.grid) {
        cat.petrushkacat.audiobookplayer.domain.models.Grid.LIST -> Icons.Default.GridView
        cat.petrushkacat.audiobookplayer.domain.models.Grid.BIG_CELLS -> Icons.Default.GridOn
        cat.petrushkacat.audiobookplayer.domain.models.Grid.SMALL_CELLS -> Icons.Default.ListAlt
    }
    TopAppBar(
        navigationIcon = {
            Row {
                Icon(Icons.Default.Menu,
                    stringResource(id = R.string.menu_icon_description),
                    modifier = Modifier
                        .clickable {
                            onDrawerButtonClick()
                        }
                        .size(48.dp)
                        .padding(horizontal = 10.dp)
                )
                FoldersChooser(folders = folders, onFolderChoose = component::onFolderChange)
            }
        },
        title = { },
        actions = {
            SearchView(component::onSearch)
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        component.onGridButtonClick()
                    }
                    .size(48.dp)
                    .padding(horizontal = 10.dp)
            )
            Icon(
                Icons.Default.Folder,
                contentDescription = stringResource(id = R.string.grid_icon_description),
                modifier = Modifier
                    .clickable {
                        component.onFolderButtonClick()
                    }
                    .size(48.dp)
                    .padding(horizontal = 10.dp)
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(onSearch: (String) -> Unit) {
    val isExpanded = remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    if (!isExpanded.value) {
        Icon(
            Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search_icon_description),
            modifier = Modifier
                .clickable {
                    isExpanded.value = true
                }
                .size(48.dp)
                .padding(horizontal = 10.dp)
        )
    } else {
        SideEffect {
            focusRequester.requestFocus()
        }
        TextField(value = searchText.value,
            onValueChange = {
                searchText.value = it
                onSearch(searchText.value)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            trailingIcon = {
                Icon(Icons.Default.Close,
                    stringResource(id = R.string.close_search_icon),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(7.dp)
                        .clickable {
                            searchText.value = ""
                            isExpanded.value = false
                            onSearch(searchText.value)
                        })
            }
        )
    }
}

@Composable
fun FoldersChooser(folders: List<RootFolderEntity>, onFolderChoose: (RootFolderEntity?) -> Unit) {
    val isExpanded = remember { mutableStateOf(false) }

    Box() {
        if (folders.isNotEmpty()) {
            val currentFolder = folders.firstOrNull {
                it.isCurrent
            }
            if (currentFolder != null) {
                FolderToolbarItem(Modifier.width(200.dp), folder = currentFolder, onClick = {
                    isExpanded.value = true
                }
                )
            } else {
                FolderToolbarItem(Modifier.width(200.dp), folder = null, onClick = {
                    isExpanded.value = true
                }
                )
            }

            if (isExpanded.value) {
                DropdownMenu(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .border(
                            BorderStroke(1.dp, Color.White), RoundedCornerShape(0.dp)
                        ),
                    expanded = isExpanded.value,
                    onDismissRequest = { isExpanded.value = false }) {
                    LazyColumn(
                        Modifier
                            .height(((folders.size + 1) * 50).dp)
                            .width(200.dp)) {
                        item {
                            FolderToolbarItem(folder = null, onClick = {folder ->
                                onFolderChoose(folder)
                                isExpanded.value = false
                                }
                            )

                        }
                        items(folders.size) {
                            FolderToolbarItem(folder = folders[it], onClick = {folder ->
                                    onFolderChoose(folder)
                                    isExpanded.value = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FolderToolbarItem(
    modifier: Modifier = Modifier,
    folder: RootFolderEntity?,
    onClick: (RootFolderEntity?) -> Unit
) {
    Column (
        modifier = modifier
            .height(50.dp)
            .clickable {
                onClick(folder)
            },
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            folder?.name ?: stringResource(id = R.string.all), modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}
