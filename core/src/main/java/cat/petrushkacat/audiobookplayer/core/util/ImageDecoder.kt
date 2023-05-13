package cat.petrushkacat.audiobookplayer.core.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun decodeImage(data: ByteArray?): Bitmap? {
    return if(data != null) {
        BitmapFactory.decodeByteArray(data, 0 , data.size)
    } else {
        null
    }
}