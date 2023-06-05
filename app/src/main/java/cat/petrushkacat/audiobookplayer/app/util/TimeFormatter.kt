package cat.petrushkacat.audiobookplayer.app.util

import android.content.Context
import cat.petrushkacat.audiobookplayer.R
import java.util.concurrent.TimeUnit

fun formatDuration(duration: Long): String {
    val hours: Long = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS)

    val minutes: Long = (TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
            - hours * TimeUnit.MINUTES.convert(1, TimeUnit.HOURS))

    val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
            - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
            - hours * TimeUnit.SECONDS.convert(1, TimeUnit.HOURS))

    return if(hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else if(minutes> 0) {
        String.format("%02d:%02d", minutes, seconds)
    } else {
        String.format("0:%02d", seconds)
    }
}

fun formatDurationAlt(duration: Long, context: Context): String {
    val hours: Long = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS)

    val minutes: Long = (TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
            - hours * TimeUnit.MINUTES.convert(1, TimeUnit.HOURS))

    val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
            - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
            - hours * TimeUnit.SECONDS.convert(1, TimeUnit.HOURS))

    var timeText = ""

    if(hours != 0L) timeText += "${hours}${context.getString(R.string.h)} "
    if(minutes != 0L) timeText += "${minutes}${context.getString(R.string.m)} "
    if(seconds != 0L) timeText += "${seconds}${context.getString(R.string.s)} "
    return timeText
}