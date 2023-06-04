package cat.petrushkacat.audiobookplayer.data.db.type_converters

import android.util.Log
import androidx.room.TypeConverter
import cat.petrushkacat.audiobookplayer.data.dto.Chapters
import cat.petrushkacat.audiobookplayer.data.dto.Notes
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AudiobookTypeConverters {

    @TypeConverter
    fun fromChaptersDBToJson(chapters: Chapters): String {
        return Json.encodeToString(chapters)
    }

    @TypeConverter
    fun fromJsonToChaptersDB(json: String): Chapters {
        Log.d("type converters chapters income", json)

        val result: Chapters? = try{
            Json.decodeFromString<Chapters>(json)
        } catch (e: Exception) {
            null
        }

        return if (result == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"chapters\"")
                .replace("\"a\"", "\"bookFolderUri\"")
                .replace("\"b\"", "\"name\"")
                .replace("\"c\"", "\"duration\"")
                .replace("\"d\"", "\"timeFromBeginning\"")
                .replace("\"e\"", "\"uri\"")
            Log.d("type converters chapters result if obfuscated", newString)
            Json.decodeFromString<Chapters>(newString)
        } else {
            result
        }
    }

    @TypeConverter
    fun fromNotesDBToJson(notes: Notes): String {
        return Json.encodeToString(notes)
    }

    @TypeConverter
    fun fromJsonToNotesDB(json: String): Notes {
        Log.d("type converters notes income", json)

        val result: Notes? = try{
            Json.decodeFromString<Notes>(json)
        } catch (e: Exception) {
            null
        }

        return if (result == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"notes\"")
                .replace("\"a\"", "\"chapterIndex\"")
                .replace("\"b\"", "\"chapterName\"")
                .replace("\"c\"", "\"time\"")
                .replace("\"d\"", "\"description\"")
            Log.d("type converters notes result if obfuscated", newString)
            Json.decodeFromString<Notes>(newString)
        } else {
            result
        }
    }

    //----------------------------------

    @TypeConverter
    fun fromChaptersToJson(chapters: cat.petrushkacat.audiobookplayer.domain.models.Chapters): String {
        return Json.encodeToString(chapters)
    }

    @TypeConverter
    fun fromJsonToChapters(json: String): cat.petrushkacat.audiobookplayer.domain.models.Chapters {
        Log.d("type converters chapters income", json)

        val result: cat.petrushkacat.audiobookplayer.domain.models.Chapters? = try{
            Json.decodeFromString<cat.petrushkacat.audiobookplayer.domain.models.Chapters>(json)
        } catch (e: Exception) {
            null
        }

        return if (result == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"chapters\"")
                .replace("\"a\"", "\"bookFolderUri\"")
                .replace("\"b\"", "\"name\"")
                .replace("\"c\"", "\"duration\"")
                .replace("\"d\"", "\"timeFromBeginning\"")
                .replace("\"e\"", "\"uri\"")
            Log.d("type converters chapters result if obfuscated", newString)
            Json.decodeFromString<cat.petrushkacat.audiobookplayer.domain.models.Chapters>(newString)
        } else {
            result
        }
    }

    @TypeConverter
    fun fromNotesToJson(notes: cat.petrushkacat.audiobookplayer.domain.models.Notes): String {
        return Json.encodeToString(notes)
    }

    @TypeConverter
    fun fromJsonToNotes(json: String): cat.petrushkacat.audiobookplayer.domain.models.Notes {
        Log.d("type converters notes income", json)

        val result: cat.petrushkacat.audiobookplayer.domain.models.Notes? = try{
            Json.decodeFromString<cat.petrushkacat.audiobookplayer.domain.models.Notes>(json)
        } catch (e: Exception) {
            null
        }

        return if (result == null) {
            val newString = json
                .replaceFirst("\"a\"", "\"notes\"")
                .replace("\"a\"", "\"chapterIndex\"")
                .replace("\"b\"", "\"chapterName\"")
                .replace("\"c\"", "\"time\"")
                .replace("\"d\"", "\"description\"")
            Log.d("type converters notes result if obfuscated", newString)
            Json.decodeFromString<cat.petrushkacat.audiobookplayer.domain.models.Notes>(newString)
        } else {
            result
        }
    }
}