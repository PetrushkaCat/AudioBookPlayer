package cat.petrushkacat.audiobookplayer.audioservice

interface TimeUpdateRepository {
    suspend fun updateTime(updateTime: UpdateTime)
}