import com.android.tools.r8.internal.ks
import org.gradle.kotlin.dsl.annotationProcessor

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tenshi18.imeiimsidecoder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tenshi18.imeiimsidecoder"
        minSdk = 30
        targetSdk = 35
        versionCode = 2
        versionName = "0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.material.android)
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation Compose для Bottom Navigation Bar
    implementation(libs.androidx.navigation.compose)

    // SQLite JDBC для импорта данных из .sqlite3 файлов
    implementation(libs.sqlite.jdbc)

    // Библиотека для Material You (Dynamic Colours)
    implementation(libs.androidx.material3.v120)

    // Корутины
    implementation(libs.kotlinx.coroutines.android)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Preferences DataStore для сохранения настроек
    implementation(libs.datastore.preferences)

    // Room для работы с БД
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    
    // moshi для работы с json (история)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
}