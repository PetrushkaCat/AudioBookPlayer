package cat.petrushkacat.audiobookplayer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cat.petrushkacat.audiobookplayer.app.scanner.Scanner
import cat.petrushkacat.audiobookplayer.app.ui.components.RootComponentUi
import cat.petrushkacat.audiobookplayer.app.ui.theme.AudioBookPlayerTheme
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.sensors.SensorListener
import cat.petrushkacat.audiobookplayer.components.components.RootComponentImpl
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import com.arkivanov.decompose.defaultComponentContext
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var audiobookServiceHandler: AudiobookServiceHandler

    @Inject
    lateinit var sensorListener: SensorListener

    @Inject
    lateinit var useCasesProvider: UseCasesProvider

    @Inject
    lateinit var scanner: Scanner

    private val isDarkTheme = MutableStateFlow(true)

    private var job = Job()

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE
    private val installStateUpdatedListener = InstallStateUpdatedListener {
        if(it.installStatus() == InstallStatus.DOWNLOADED) {
            appUpdateManager.completeUpdate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(installStateUpdatedListener)
        checkForUpdates()

        val root = RootComponentImpl(
            defaultComponentContext(),
            this,
            useCasesProvider,
            audiobookServiceHandler,
            sensorListener
        )

        CoroutineScope(job + Dispatchers.Default).launch {
            useCasesProvider.settingsUseCases.getSettingsUseCase().takeWhile { job.isActive }
                .collect {
                    isDarkTheme.value =
                        it.theme == cat.petrushkacat.audiobookplayer.domain.models.Theme.DARK
                }
        }

        setContent {
            AudioBookPlayerTheme(
                darkTheme = isDarkTheme.collectAsState().value
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootComponentUi(component = root)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        scanner.scan()
    }

    override fun onDestroy() {
        job.cancel()
        appUpdateManager.unregisterListener(installStateUpdatedListener)
        super.onDestroy()
    }

    private fun checkForUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }

            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlow(
                    info,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                        .setAllowAssetPackDeletion(false)
                        .build(),
                )
            }
        }
    }
}