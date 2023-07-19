package cat.petrushkacat.audiobookplayer.components.util

import androidx.documentfile.provider.DocumentFile
import cat.petrushkacat.audiobookplayer.domain.models.Chapter

fun extractInt(chapter: Chapter): Int {
    val num = chapter.name.replace("\\D".toRegex(), "")

    // return 0 if no digits found
    return try {
        if (num.isEmpty()) {
            0
        }
        //return the number if it's likely 1 2 3 .... 10 11 .... 101
        else if(num.length <= 3) {
            Integer.parseInt(num)
        }
        else {
            var temp = num
            temp += "0".repeat(9 - temp.length)

            Integer.parseInt(temp)
        }
    } catch (e: Exception) {
        Integer.parseInt(num.substring(0, 9))
    }
}

fun DocumentFile.isAudio(): Boolean {
    if(!isFile) return false
    val name = name ?: return false
    if(name.substringAfterLast(".").lowercase() in supportedAudioFormats) return true
    return false
}

fun DocumentFile.isImage(): Boolean {
    if(!isFile) return false
    val name = name ?: return false
    if(name.substringAfterLast(".").lowercase() in supportedImageFormats ) return true
    return false
}