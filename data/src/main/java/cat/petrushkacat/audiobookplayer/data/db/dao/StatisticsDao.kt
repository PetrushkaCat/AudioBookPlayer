package cat.petrushkacat.audiobookplayer.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cat.petrushkacat.audiobookplayer.data.dto.StatisticsEntityDB
import cat.petrushkacat.audiobookplayer.domain.models.Month
import cat.petrushkacat.audiobookplayer.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {

    @Query("SELECT * FROM StatisticsEntity")
    fun getAll(): Flow<List<StatisticsEntity>>

    @Query("SELECT * FROM StatisticsEntity WHERE year = :year AND month = :month AND day = :day")
    fun getDetails(year: Int, month: Int, day: Int): Flow<StatisticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(statisticsEntityDB: StatisticsEntityDB)

    @Query("SELECT * FROM StatisticsEntity")
    fun getAllMonths(): Flow<List<Month>>

    @Query("SELECT * FROM StatisticsEntity WHERE year = :year AND month = :month")
    fun getAllStatisticsInMonth(year: Int, month: Int): Flow<List<StatisticsEntity>>

}