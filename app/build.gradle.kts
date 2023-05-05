plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    //id ("com.google.devtools.ksp")
    //id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "cat.petrushkacat.audiobookplayer"
    compileSdk = 33

    defaultConfig {
        applicationId = "cat.petrushkacat.audiobookplayer"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":audioservice"))


    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.datastore.preferences)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.prewiew)
    implementation(libs.compose.material.icons.core)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3.window.size)
    implementation(libs.activity.compose)
    implementation(libs.coil.compose)

    implementation(libs.decompose)
    implementation(libs.decompose.compose)

    /*implementation ("com.arkivanov.mvikotlin:mvikotlin:$mvikotlinVersion")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-main:$mvikotlinVersion")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-logging:$mvikotlinVersion")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-timetravel:$mvikotlinVersion")
    implementation ("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$mvikotlinVersion")*/

    /*implementation(libs.koin.core)
    implementation(libs.koin.android)*/

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    debugImplementation(libs.leekcanary.android)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
}