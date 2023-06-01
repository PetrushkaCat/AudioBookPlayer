package cat.petrushkacat.audiobookplayer.audioservice.repository

import cat.petrushkacat.audiobookplayer.audioservice.model.AudioServiceSettings
import kotlinx.coroutines.flow.Flow

interface AudioServiceSettingsRepository {

    suspend fun getAudioServiceSettings(): Flow<AudioServiceSettings>
}