package cat.petrushkacat.audiobookplayer.data.repository

import cat.petrushkacat.audiobookplayer.audioservice.UpdateTime
import cat.petrushkacat.audiobookplayer.audioservice.repository.TimeUpdateRepository
import cat.petrushkacat.audiobookplayer.data.db.dao.TimeUpdateDao

class TimeUpdateRepositoryImpl(
    private val timeUpdateDao: TimeUpdateDao
): TimeUpdateRepository {
    override suspend fun updateTime(updateTime: UpdateTime) {
        timeUpdateDao.updateTime(updateTime)
    }
}