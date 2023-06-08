package cat.petrushkacat.audiobookplayer.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer.DrawerComponent

@Composable
fun DrawerComponentUi(component: DrawerComponent) {

    val context = LocalContext.current
    val version = context.packageManager.getPackageInfo(context.packageName, 1).versionName

    val settings by component.settings.collectAsState()
    val wasRated by component.wasRated.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            HeaderDrawerItem(
                icon = painterResource(R.drawable.ic_launcher),
                onClick = { },
                text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.app_name)
            )
            Divider(modifier = Modifier.fillMaxWidth())
        }
        Column {
            Column {
                DrawerItem(
                    icon = Icons.Default.Favorite,
                    text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.favorites),
                    onClick = {
                        component.onFavoritesClick()
                    })

                DrawerItem(
                    icon = Icons.Default.WatchLater,
                    text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.listen_later),
                    onClick = {
                        component.onListenLaterClick()
                    })
                DrawerItem(
                    icon = Icons.Default.CheckCircle,
                    text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.completed_books),
                    onClick = {
                        component.onCompletedBooksClick()
                    })

                DrawerItem(
                    icon = Icons.Default.BarChart,
                    text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.statistics),
                    onClick = {
                        component.onStatisticsClick()
                    })

                DrawerItem(
                    icon = Icons.Default.Settings,
                    text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.settings),
                    onClick = {
                        component.onSettingsClick()
                    },
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.settings_icon_description)
                )

                if (settings.isBugReportButtonEnabled) {
                    DrawerItem(
                        icon = Icons.Default.BugReport,
                        text = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.suggestions_and_bug_report),
                        onClick = {
                            component.onBugReportClick()
                        })
                }
                if (settings.isReviewButtonEnabled) {
                    val text = if (wasRated) {
                        stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.thanks_for_the_review)
                    } else {
                        stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.rate_this_app)
                    }
                    DrawerItem(
                        icon = Icons.Default.StarRate,
                        text = text,
                        onClick = {
                            component.onRateClick()
                        })
                }

                Divider(modifier = Modifier.fillMaxWidth())
            }
            Column(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                Row(
                    modifier = Modifier.width(180.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.designed_with_paws),
                        style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Icon(
                        Icons.Default.Pets,
                        stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.paws_icon),
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Row(
                    modifier = Modifier.width(180.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.by_petrushka_cat),
                        style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                    )
                    //Icon(Icons.Default.LocalFlorist, null)
                }
                Text(
                    stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.version) + " " + version,
                    style = TextStyle(color = Color.Gray, fontSize = 13.sp)
                )
            }
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, text: String, onClick: () -> Unit, iconDescription: String = "") {
    Column(modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()
        .height(50.dp)
        .padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Icon(
                icon,
                iconDescription,
                modifier = Modifier
                    .padding(4.dp)
                    .size(23.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text, style = TextStyle(fontSize = 17.sp), modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun HeaderDrawerItem(icon: Painter, text: String, onClick: () -> Unit, iconDescription: String = "") {
    Column(modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()
        .height(50.dp)
        .padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Image(
                icon,
                iconDescription,
                modifier = Modifier
                    .padding(4.dp)
                    .size(23.dp)
                    .background(color = Color.Transparent, shape = RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(text, style = TextStyle(fontSize = 17.sp), modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}