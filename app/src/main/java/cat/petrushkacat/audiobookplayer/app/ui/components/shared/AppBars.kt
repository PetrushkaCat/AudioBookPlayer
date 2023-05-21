package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cat.petrushkacat.audiobookplayer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    title: String,
    onBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = { Icon(
            Icons.Default.ArrowBack,
            stringResource(id = R.string.back),
            modifier = Modifier
                .clickable {
                    onBack()
                }
                .size(48.dp)
                .padding(horizontal = 8.dp)
        )
        },
        title = {
            Row {
                Spacer(Modifier.width(15.dp))
                Text(title)
            }
        }
    )
}