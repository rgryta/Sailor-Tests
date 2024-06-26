import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.realm)
}

val versions = Properties()
file("version.properties").inputStream().use { stream ->
    versions.load(stream)
}

android {
    namespace = "eu.gryta.sailortests"
    compileSdk = 34

    defaultConfig {
        applicationId = "eu.gryta.sailortests"
        minSdk = 26
        targetSdk = 34
        versionCode = versions.getProperty("versionCode").toInt()
        versionName = versions.getProperty("versionName")

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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.number.picker)

    implementation(libs.androidx.ui.text.google.fonts)

    implementation(libs.material)
    implementation(libs.material.icons)
    implementation(libs.material3)
    implementation(libs.material3.window)

    implementation(libs.realm)
    implementation(libs.opencsv)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}