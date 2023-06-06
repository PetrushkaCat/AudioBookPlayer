package cat.petrushkacat.audiobookplayer.audioservice.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.GregorianCalendar

class SensorListener(
   private val getSettingsUseCase: GetSettingsUseCase
): SensorEventListener {

    var timeToStop: Long = Long.MAX_VALUE

    private val settings: MutableStateFlow<SettingsEntity> = MutableStateFlow(SettingsEntity())

    private var lastValues: FloatArray? = floatArrayOf(0f)
    private var isTimerSet = false

    init {
        CoroutineScope(Dispatchers.Default).launch {
            getSettingsUseCase().collect {
                settings.value = it
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(lastValues.contentEquals(event?.values)) {
            if(!isTimerSet) {
                timeToStop = GregorianCalendar().timeInMillis + settings.value.autoSleepTime
                isTimerSet = true
            }
            //Log.d("sensor", "sensor event")
        } else {
            lastValues = event?.values
            isTimerSet = false
            //Log.d("sensor", "sensor event 2")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}
