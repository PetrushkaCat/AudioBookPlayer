package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material.icons.twotone.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.R
import cat.petrushkacat.audiobookplayer.app.util.formatDuration
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val favoriteIconColor = Color(0xFFFF8A8A)
private val listenLaterIconColor = Color(0xFFFFF387)
private val completedIconColor = Color(0xFFA8FFA2)


@Composable
fun BookListItem(
    model: BookListEntity,
    modifier: Modifier
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = model.image) {
        scope.launch(Dispatchers.IO) {
            bitmap.value = if (model.image != null) {
                BitmapFactory.decodeByteArray(model.image, 0, model.image!!.size)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.round_play_button)
            }
        }
    }

    Column(
        modifier = modifier
            .height(150.dp)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            if (bitmap.value != null) {
                Box {
                    Image(
                        modifier = Modifier
                            .size(100.dp),
                        bitmap = bitmap.value!!.asImageBitmap(),
                        contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.book_cover),
                    )
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        if (model.isWantToListen) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.TwoTone.WatchLater,
                                    null,
                                    modifier = Modifier
                                        .size(27.dp),
                                    tint = Color.Black
                                )
                                Icon(
                                    Icons.TwoTone.WatchLater,
                                    null,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = listenLaterIconColor
                                )
                            }
                        }
                        if (model.isCompleted) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.TwoTone.CheckCircle,
                                    null,
                                    modifier = Modifier
                                        .size(27.dp),
                                    tint = Color.Black
                                )
                                Icon(
                                    Icons.TwoTone.CheckCircle,
                                    null,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = completedIconColor
                                )
                            }
                        }
                        if (model.isFavorite) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    null,
                                    modifier = Modifier
                                        .size(27.dp),
                                    tint = Color.Black
                                )
                                Icon(
                                    Icons.Filled.Favorite,
                                    null,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = favoriteIconColor
                                )
                            }
                        }
                    }
                }
            }
            Text(
                model.name,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Column {
            val duration = if(model.duration != 0L) model.duration else 1
            LinearProgressIndicator(
                progress = model.currentTime.toFloat() / duration,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatDuration(model.currentTime), style = TextStyle(fontSize = 12.sp))
                Text(formatDuration(model.duration), style = TextStyle(fontSize = 12.sp))
            }
        }
    }
}

@Composable
fun BookGridItem(model: BookListEntity, modifier: Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(key1 = model.image) {
        scope.launch(Dispatchers.IO) {
            bitmap.value = if (model.image != null) {
                BitmapFactory.decodeByteArray(model.image, 0, model.image!!.size)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.round_play_button)
            }
        }
    }

    Box(modifier = modifier.padding(4.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (bitmap.value != null) {
                Box {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(),
                            //.padding(horizontal = 12.dp),
                        bitmap = bitmap.value!!.asImageBitmap(),
                        contentDescription = stringResource(id = cat.petrushkacat.audiobookplayer.strings.R.string.book_cover),
                    )
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        if (model.isWantToListen) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.TwoTone.WatchLater,
                                    null,
                                    modifier = Modifier
                                        .size(27.dp),
                                    tint = Color.Black
                                )
                                Icon(
                                    Icons.TwoTone.WatchLater,
                                    null,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = listenLaterIconColor
                                )
                            }
                        }
                        if (model.isCompleted) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.TwoTone.CheckCircle,
                                    null,
                                    modifier = Modifier
                                        .size(27.dp),
                                    tint = Color.Black
                                )
                                Icon(
                                    Icons.TwoTone.CheckCircle,
                                    null,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = completedIconColor
                                )
                            }
                        }
                        if (model.isFavorite) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Filled.Favorite,
                                    null,
                                    modifier = Modifier
                                        .size(27.dp),
                                    tint = Color.Black
                                )
                                Icon(
                                    Icons.Filled.Favorite,
                                    null,
                                    modifier = Modifier
                                        .size(24.dp),
                                    tint = favoriteIconColor
                                )
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.defaultMinSize(minHeight = 72.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(model.name, maxLines = 3, style = TextStyle(fontSize = 14.sp))
                Column {
                    val duration = if(model.duration != 0L) model.duration else 1
                    LinearProgressIndicator(progress = model.currentTime.toFloat() / duration)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(formatDuration(model.currentTime), style = TextStyle(fontSize = 12.sp))
                        Text(formatDuration(model.duration), style = TextStyle(fontSize = 12.sp))
                    }
                }
            }
        }

    }
}