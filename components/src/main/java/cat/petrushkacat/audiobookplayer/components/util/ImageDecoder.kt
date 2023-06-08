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
    val scale = if(bitmap.width > 500) {
        bitmap.width / 500.0
    } else 1.0
    val width = bitmap.width / scale
    val height = bitmap.height / scale
    val scaledBitmap = bitmap.scale(width.toInt(), height.toInt())
    val newBitmap: Bitmap = if(scaledBitmap.width >= 500 && scaledBitmap.height >= 500) {
         Bitmap.createBitmap(
            scaledBitmap,
            0,
            (scaledBitmap.height - 499) / 2,
            500,
            500
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
    newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
    return stream.toByteArray()
}