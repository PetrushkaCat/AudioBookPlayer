plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id ("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "1.8.21"

}

android {
    namespace = "cat.petrushkacat.audiobookplayer.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    /*kotlin {
        jvmToolchain(11)
    }*/
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":components"))
    implementation(project(":audioservice"))

    implementation (libs.androidx.core.ktx)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.gson)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.room.testing)
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("app.cash.turbine:turbine:0.13.0")
    val mockkVersion = "1.13.5"
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.mockk:mockk-android:${mockkVersion}")
    testImplementation("io.mockk:mockk-agent:${mockkVersion}")


    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.46")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.46")

    androidTestImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation ("com.google.truth:truth:1.1.3")
    androidTestImplementation ("androidx.test:core-ktx:1.5.0")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("io.mockk:mockk-android:${mockkVersion}")
    androidTestImplementation ("io.mockk:mockk-agent:${mockkVersion}")


}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        // Note: If you're using KSP, change the line below to return
        // listOf("room.schemaLocation=${schemaDir.path}").
        return listOf("room.schemaLocation=${schemaDir.path}")
    }
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}