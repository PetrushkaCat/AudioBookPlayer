package cat.petrushkacat.audiobookplayer.data.db

import android.util.Log
import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.data.dto.Chapters
import cat.petrushkacat.audiobookplayer.data.dto.Notes
import com.google.gson.Gson

class AudiobookTypeConverters {

    @TypeConverter
    fun fromChaptersDBToJson(chapters: Chapters): String {
        return Gson().toJson(chapters)
    }

    @TypeConverter
    fun fromJsonToChaptersDB(json: String): Chapters {
        val result = Gson().fromJson(json, Chapters::class.java)
        Log.d("type converters chapters income", json)

        return if (result.chapters == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"chapters\"")
                .replace("\"a\"", "\"bookFolderUri\"")
                .replace("\"b\"", "\"name\"")
                .replace("\"c\"", "\"duration\"")
                .replace("\"d\"", "\"timeFromBeginning\"")
                .replace("\"e\"", "\"uri\"")
            Log.d("type converters chapters result if obfuscated", newString)
            Gson().fromJson(newString, Chapters::class.java)
        } else {
            result
        }
    }

    @TypeConverter
    fun fromJsonToNotesDB(json: String): Notes {
        val result = Gson().fromJson(json, Notes::class.java)
        Log.d("type converters notes income", json)

        return if (result.notes == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"notes\"")
                .replace("\"a\"", "\"chapterIndex\"")
                .replace("\"b\"", "\"chapterName\"")
                .replace("\"c\"", "\"time\"")
                .replace("\"d\"", "\"description\"")
            Log.d("type converters notes result if obfuscated", newString)
            Gson().fromJson(newString, Notes::class.java)
        } else {
            result
        }
    }

    @TypeConverter
    fun fromNotesDBToJson(notes: Notes): String {
        return Gson().toJson(notes)
    }

    //----------------------------------

    @TypeConverter
    fun fromChaptersDBToJson(chapters: cat.petrushkacat.audiobookplayer.domain.models.Chapters): String {
        return Gson().toJson(chapters)
    }

    @TypeConverter
    fun fromJsonToChapters(json: String): cat.petrushkacat.audiobookplayer.domain.models.Chapters {
        val result = Gson().fromJson(json, cat.petrushkacat.audiobookplayer.domain.models.Chapters::class.java)
        Log.d("type converters chapters income", json)

        return if (result.chapters == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"chapters\"")
                .replace("\"a\"", "\"bookFolderUri\"")
                .replace("\"b\"", "\"name\"")
                .replace("\"c\"", "\"duration\"")
                .replace("\"d\"", "\"timeFromBeginning\"")
                .replace("\"e\"", "\"uri\"")
            Log.d("type converters chapters result if obfuscated", newString)
            Gson().fromJson(newString, cat.petrushkacat.audiobookplayer.domain.models.Chapters::class.java)
        } else {
            result
        }
    }

    @TypeConverter
    fun fromJsonToNotes(json: String): cat.petrushkacat.audiobookplayer.domain.models.Notes {
        val result = Gson().fromJson(json, cat.petrushkacat.audiobookplayer.domain.models.Notes::class.java)
        Log.d("type converters notes income", json)

        return if (result.notes == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"notes\"")
                .replace("\"a\"", "\"chapterIndex\"")
                .replace("\"b\"", "\"chapterName\"")
                .replace("\"c\"", "\"time\"")
                .replace("\"d\"", "\"description\"")
            Log.d("type converters notes result if obfuscated", newString)
            Gson().fromJson(newString, cat.petrushkacat.audiobookplayer.domain.models.Notes::class.java)
        } else {
            result
        }
    }

    @TypeConverter
    fun fromNotesToJson(notes: cat.petrushkacat.audiobookplayer.domain.models.Notes): String {
        return Gson().toJson(notes)
    }
}