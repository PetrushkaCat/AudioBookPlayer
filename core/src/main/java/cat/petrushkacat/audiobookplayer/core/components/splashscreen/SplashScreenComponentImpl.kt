package cat.petrushkacat.audiobookplayer.core.components.splashscreen

import android.util.Log

class SplashScreenComponentImpl(
    private val onAnimationEnd: () -> Unit
) : SplashScreenComponent {

    init {
        Log.d("init", "splash")
    }

    override fun onAnimationEnded() {
        onAnimationEnd()
    }
}