package cat.petrushkacat.audiobookplayer.audioservice.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import java.util.GregorianCalendar

class SensorListener: SensorEventListener {

    var timeToStop: Long = Long.MAX_VALUE

    private var lastValues: FloatArray? = floatArrayOf(0f)
    private var isTimerSet = false

    override fun onSensorChanged(event: SensorEvent?) {
        if(lastValues.contentEquals(event?.values)) {
            if(!isTimerSet) {
                timeToStop = GregorianCalendar().timeInMillis + 60000 * 60 * 2
                isTimerSet = true
            }
            //Log.d("sensor", "sensor event")
        } else {
            lastValues = event?.values
            isTimerSet = false
            Log.d("sensor", "sensor event 2")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}
