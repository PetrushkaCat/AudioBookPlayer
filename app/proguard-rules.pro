#-dontobfuscate

-keep class hilt_aggregated_deps.** { *; }

-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn org.robolectric.shadows.CoreShadowsAdapter

-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.net.ssl.OpenJSSE

#-keep,allowoptimization class androidx.preference.** { public protected *; }
#-keep,allowoptimization class kotlin.** { public protected *; }
#-keep,allowoptimization class kotlinx.coroutines.** { public protected *; }
#-keep,allowoptimization class kotlinx.serialization.** { public protected *; }

##---------------Begin: proguard configuration for kotlinx.serialization  ----------
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
   *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class cat.petrushkacat.audiobookplayer.**$$serializer { *; }
-keepclassmembers class cat.petrushkacat.audiobookplayer.** {
    *** Companion;
}
-keepclasseswithmembers class cat.petrushkacat.audiobookplayer.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep class kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.** {
    <methods>;
}
##---------------End: proguard configuration for kotlinx.serialization  ----------
