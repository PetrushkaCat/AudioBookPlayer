package cat.petrushkacat.audiobookplayer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.petrushkacat.audiobookplayer.data.dto.SettingsEntityDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSettings(settingsEntity: SettingsEntityDTO)

    @Query("SELECT * FROM SettingsEntity")
    fun getSettings(): Flow<SettingsEntityDTO?>

    @Query("SELECT * FROM SettingsEntity")
    fun getAudioServiceSettings(): Flow<SettingsEntityDTO?>
}