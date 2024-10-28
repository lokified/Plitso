plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.perfomance)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.google.services)
    alias(libs.plugins.secrets)
}

android {
    namespace = "com.loki.plitso"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.loki.plitso"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.bundles.test.common)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.lifecycle)
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.splash.screen)
    implementation(libs.timber)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.googleAuth)
    implementation(libs.bundles.worker)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore)
    implementation(libs.pager)
    implementation(libs.palette)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.bundles.retrofit)
    implementation(libs.markdown.renderer)
    implementation(libs.generativeai)
    implementation(kotlin("reflect"))
}
