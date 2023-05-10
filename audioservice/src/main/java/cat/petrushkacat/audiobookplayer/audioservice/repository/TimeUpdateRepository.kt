package cat.petrushkacat.audiobookplayer.audioservice.repository

import cat.petrushkacat.audiobookplayer.audioservice.UpdateTime

interface TimeUpdateRepository {
    suspend fun updateTime(updateTime: UpdateTime)
}