plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "cat.petrushkacat.audiobookplayer.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

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
}

dependencies {

    implementation(project(":audioservice"))
    
    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)

    /*implementation(libs.koin.core)
    implementation(libs.koin.android)*/

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.androidx.documentfile)
    implementation("androidx.room:room-common:2.5.1")

    implementation(libs.media3.session)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

}