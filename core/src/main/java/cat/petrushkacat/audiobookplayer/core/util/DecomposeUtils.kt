package cat.petrushkacat.audiobookplayer.core.util

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun ComponentContext.componentCoroutineScopeMain(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}

fun ComponentContext.componentCoroutineScopeIO(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}

fun ComponentContext.componentCoroutineScopeDefault(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}

fun <T : Any> Value<T>.toStateFlow(lifecycle: Lifecycle): StateFlow<T> {
    val state = MutableStateFlow(this.value)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        val observer = { value: T -> state.value = value }
        subscribe(observer)
        lifecycle.doOnDestroy {
            unsubscribe(observer)
        }
    }

    return state
}