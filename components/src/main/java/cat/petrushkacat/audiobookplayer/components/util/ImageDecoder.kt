package cat.petrushkacat.audiobookplayer.components.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

fun decodeImage(data: ByteArray?): Bitmap? {
    return if(data != null) {
        BitmapFactory.decodeByteArray(data, 0 , data.size)
    } else {
        null
    }
}

fun arrayFromBitmap(bitmap: Bitmap): ByteArray {
    val scale = if(bitmap.width > 1000) {
        bitmap.width / 1000.0
    } else 1.0
    val width = bitmap.width / scale
    val height = bitmap.height / scale
    val scaledBitmap = bitmap.scale(width.toInt(), height.toInt())
    val newBitmap: Bitmap = if(scaledBitmap.width >= 1000 && scaledBitmap.height >= 1000) {
         Bitmap.createBitmap(
            scaledBitmap,
            0,
            (scaledBitmap.height - 999) / 2,
            1000,
            1000
        )
    } else if (scaledBitmap.width > scaledBitmap.height) {
        Bitmap.createBitmap(
            scaledBitmap,
            (scaledBitmap.width - scaledBitmap.height) / 2,
            0,
            scaledBitmap.height,
            scaledBitmap.height
        )
    } else if (scaledBitmap.width < scaledBitmap.height) {
        Bitmap.createBitmap(
            scaledBitmap,
            0,
            (scaledBitmap.height - scaledBitmap.width) / 2,
            scaledBitmap.width,
            scaledBitmap.width
        )
    } else {
        scaledBitmap
    }
    val stream = ByteArrayOutputStream()
    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}