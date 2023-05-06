package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.audioservice.TimeUpdateRepository
import cat.petrushkacat.audiobookplayer.audioservice.UpdateTime
import cat.petrushkacat.audiobookplayer.data.db.TimeUpdateDao

class TimeUpdateRepositoryImpl(
    private val timeUpdateDao: TimeUpdateDao
): TimeUpdateRepository {
    override suspend fun updateTime(updateTime: UpdateTime) {
        timeUpdateDao.updateTime(updateTime)
    }
}