package cat.petrushkacat.audiobookplayer.core.components.main.bookshelf.drawer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.arkivanov.decompose.ComponentContext

class DrawerComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    private val onSettingsClicked: () -> Unit
) : DrawerComponent, ComponentContext by componentContext {

    override fun onSettingsClick() {
        onSettingsClicked()
    }

    override fun onRateClick() {
            val uri = Uri.parse("market://details?id=" + context.packageName)
            val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)

            var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

            goToMarketIntent.addFlags(flags)

            try {
                startActivity(context, goToMarketIntent, null)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName))

                startActivity(context, intent, null)
            }

    }
}