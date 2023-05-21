package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.toolbar.BookshelfToolbarComponent
import cat.petrushkacat.audiobookplayer.core.models.Grid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfToolbarComponentUi(
    component: BookshelfToolbarComponent,
    onDrawerButtonClick: () -> Unit
) {
    val settings = component.settings.collectAsState()

    val icon = when (settings.value.grid) {
        Grid.LIST -> Icons.Default.GridView
        Grid.BIG_CELLS -> Icons.Default.GridOn
        Grid.SMALL_CELLS -> Icons.Default.ListAlt
    }
    TopAppBar(
        navigationIcon = {
            Icon(Icons.Default.Menu,
                stringResource(id = R.string.menu_icon_description),
                modifier = Modifier
                    .clickable {
                        onDrawerButtonClick()
                    }
                    .size(48.dp)
                    .padding(horizontal = 10.dp)
            )
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

    if(!isExpanded.value) {
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
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background
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
