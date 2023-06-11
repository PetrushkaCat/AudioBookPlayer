package cat.petrushkacat.audiobookplayer.audioservice.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import cat.petrushkacat.audiobookplayer.audioservice.R

@UnstableApi
class AudiobookNotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?
) : PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.mediaMetadata.albumTitle ?: ""
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return pendingIntent
    }

    override fun getCurrentContentText(player: Player): CharSequence {
        return player.mediaMetadata.title ?: ""
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        val bitmap = if(player.mediaMetadata.artworkData != null) {
            BitmapFactory.decodeByteArray(
                player.mediaMetadata.artworkData,
                0,
                player.mediaMetadata.artworkData!!.size
            )
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.round_play_button)
        }
        return bitmap
    }
}