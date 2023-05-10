package cat.petrushkacat.audiobookplayer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.petrushkacat.audiobookplayer.audioservice.model.AudioServiceSettings
import cat.petrushkacat.audiobookplayer.core.models.SettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSettings(settingsEntity: SettingsEntity)

    @Query("SELECT * FROM SettingsEntity")
    fun getSettings(): Flow<SettingsEntity>

    @Query("SELECT * FROM SettingsEntity")
    fun getAudioServiceSettings(): Flow<AudioServiceSettings>
}