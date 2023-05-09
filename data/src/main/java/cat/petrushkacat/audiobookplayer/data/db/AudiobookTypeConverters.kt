package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.core.models.Chapters
import cat.petrushkacat.audiobookplayer.core.models.Notes
import com.google.gson.Gson

class AudiobookTypeConverters {

    @TypeConverter
    fun fromChaptersToJson(chapters: Chapters): String {
        return Gson().toJson(chapters)
    }

    @TypeConverter
    fun fromJsonToChapters(json: String): Chapters {
        return Gson().fromJson(json, Chapters::class.java)
    }

    @TypeConverter
    fun fromJsonToNotes(json: String): Notes {
        return Gson().fromJson(json, Notes::class.java)
    }

    @TypeConverter
    fun fromNotesToJson(notes: Notes): String {
        return Gson().toJson(notes)
    }
}