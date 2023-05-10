package cat.petrushkacat.audiobookplayer.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Countertops
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer.DrawerComponent

@Composable
fun DrawerComponentUi(component: DrawerComponent) {

    Column(modifier = Modifier
        .fillMaxHeight()
        .width(260.dp)
        .background(color = MaterialTheme.colorScheme.background)
        .scrollable(rememberScrollState(), Orientation.Vertical),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            DrawerItem(
                icon = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
                text = "Simple AudioBook Player"
            ) {

            }
            Divider(modifier = Modifier.fillMaxWidth())
        }
        Column {
            DrawerItem(icon = Icons.Default.Countertops, text = "123") {

            }
            DrawerItem(icon = Icons.Default.Settings, text = "Settings", onClick = {
                component.onSettingsClick()
            })
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(horizontal = 6.dp)
        .clickable { onClick() },
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Icon(
                icon,
                null,
                modifier = Modifier
                    .padding(4.dp)
                    .size(23.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text, style = TextStyle(fontSize = 17.sp), modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}