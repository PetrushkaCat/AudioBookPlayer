package cat.petrushkacat.audiobookplayer.core.components

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {

    val models: StateFlow<A>

    @Parcelize
    data class A(
        val a: String
    ): Parcelable
}