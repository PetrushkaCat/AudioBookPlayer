package cat.petrushkacat.audiobookplayer.components.components.main.bookshelf.drawer

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import cat.petrushkacat.audiobookplayer.components.util.componentCoroutineScopeDefault
import cat.petrushkacat.audiobookplayer.domain.models.SettingsEntity
import cat.petrushkacat.audiobookplayer.domain.usecases.settings.GetSettingsUseCase
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val BUG_REPORT_URL = "https://github.com/PetrushkaCat/AudioBookPlayer/issues"
const val SHARED_PREFS_NAME = "shared_preferences"
const val WAS_RATED_KEY = "was_rated_key"

class DrawerComponentImpl(
    componentContext: ComponentContext,
    private val context: Context,
    getSettingsUseCase: GetSettingsUseCase,
    private val onSettingsClicked: () -> Unit,
    private val onFavoritesClicked: () -> Unit,
    private val onListenLaterClicked: () -> Unit,
    private val onCompletedBooksClicked: () -> Unit,
    private val onStatisticsClicked: () -> Unit
) : DrawerComponent, ComponentContext by componentContext {

    private val scopeDefault = componentCoroutineScopeDefault()

    private val _settings = MutableStateFlow(SettingsEntity())
    override val settings: StateFlow<SettingsEntity> = _settings


    private val _wasRated = MutableStateFlow(false)
    override val wasRated: StateFlow<Boolean> = _wasRated

    init {
        scopeDefault.launch {
            getSettingsUseCase.invoke().collect {
                _settings.value = it
            }
        }
        _wasRated.value = context
            .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(WAS_RATED_KEY, false)
    }

    override fun onFavoritesClick() {
        onFavoritesClicked()
    }

    override fun onListenLaterClick() {
        onListenLaterClicked()
    }

    override fun onCompletedBooksClick() {
        onCompletedBooksClicked()
    }

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
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)
            )

            startActivity(context, intent, null)
        }

        val prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(WAS_RATED_KEY, true).apply()

        scopeDefault.launch {
            delay(2000)
            _wasRated.value = true
        }

    }

    override fun onStatisticsClick() {
        onStatisticsClicked()
    }

    override fun onBugReportClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BUG_REPORT_URL))
        //val chooser = Intent.createChooser(intent, context.getString(cat.petrushkacat.audiobookplayer.strings.R.string.open_with))
        context.startActivity(intent)
    }
}