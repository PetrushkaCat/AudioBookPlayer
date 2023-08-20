package cat.petrushkacat.audiobookplayer.components

import android.graphics.BitmapFactory
import cat.petrushkacat.audiobookplayer.components.models.BookListItem
import cat.petrushkacat.audiobookplayer.components.util.arrayFromBitmap
import cat.petrushkacat.audiobookplayer.domain.models.BookListEntity

fun BookListEntity.toPresentation(): BookListItem {
    val image = if (image != null) {
        BitmapFactory.decodeByteArray(image, 0, image!!.size)
    } else {
        null
    }
    return BookListItem(
        image = image,
        name,
        folderUri,
        currentTime,
        duration,
        isStarted,
        isCompleted,
        lastTimeListened,
        isFavorite,
        isWantToListen,
        rootFolderUri = rootFolderUri,
    )
}

fun BookListItem.toCore(): BookListEntity {
    val image = if (this.image == null) {
        null
    } else {
        arrayFromBitmap(image)
    }
    return BookListEntity(
        image = image,
        name,
        folderUri,
        currentTime,
        duration,
        isStarted,
        isCompleted,
        lastTimeListened,
        isFavorite,
        isWantToListen,
        rootFolderUri = rootFolderUri,
    )
}