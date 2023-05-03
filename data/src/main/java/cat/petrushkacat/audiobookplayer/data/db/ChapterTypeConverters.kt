package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import com.google.gson.Gson

class ChapterTypeConverters {

    @TypeConverter
    fun fromChaptersToJson(chapters: Chapters): String {
        return Gson().toJson(chapters)
    }

    @TypeConverter
    fun fromJsonToChapters(json: String): Chapters {
        return Gson().fromJson(json, Chapters::class.java)
    }
}