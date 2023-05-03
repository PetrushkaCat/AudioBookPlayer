package cat.petrushkacat.audiobookplayer.app

import android.app.Application
import androidx.room.Room
import cat.petrushkacat.audiobookplayer.data.db.AudiobooksDatabase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = Room.databaseBuilder(
            instance,
            AudiobooksDatabase::class.java, "audiobooks-database"
        ).build()
    }

    companion object {
        lateinit var instance: App

        lateinit var database: AudiobooksDatabase
    }
}