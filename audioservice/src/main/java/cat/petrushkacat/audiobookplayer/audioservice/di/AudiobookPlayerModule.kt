package cat.petrushkacat.audiobookplayer.audioservice.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import cat.petrushkacat.audiobookplayer.audioservice.AudiobookServiceHandler
import cat.petrushkacat.audiobookplayer.audioservice.notification.AudiobookNotificationManager
import cat.petrushkacat.audiobookplayer.domain.usecases.UseCasesProvider
import cat.petrushkacat.audiobookplayer.domain.usecases.books.AddNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.books.UpdateNoteUseCase
import cat.petrushkacat.audiobookplayer.domain.usecases.statistics.SaveStatisticsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@UnstableApi
@Module
@InstallIn(SingletonComponent::class)
class AudiobookPlayerModule {


    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes =
        AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
            .setUsage(C.USAGE_MEDIA)
            .build()

    @Provides
    @Singleton
    @UnstableApi
    fun providePlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer =
        ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .setTrackSelector(DefaultTrackSelector(context))
            .build()

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): AudiobookNotificationManager =
        AudiobookNotificationManager(
            context = context,
            player = player
        )

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession =
        MediaSession.Builder(context, player).build()

    @Provides
    @Singleton
    fun provideServiceHandler(
        player: ExoPlayer,
        saveStatisticsUseCase: SaveStatisticsUseCase,
        addNoteUseCase: AddNoteUseCase,
        updateNoteUseCase: UpdateNoteUseCase,
        useCasesProvider: UseCasesProvider
    ): AudiobookServiceHandler =
        AudiobookServiceHandler(
            player = player,
            saveStatisticsUseCase = saveStatisticsUseCase,
            addNoteUseCase = addNoteUseCase,
            updateNoteUseCase = updateNoteUseCase,
            getBookUseCase = useCasesProvider.booksUseCases.getBookUseCase,
            getSettingsUseCase = useCasesProvider.settingsUseCases.getSettingsUseCase
        )
}