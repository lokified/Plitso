// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.firebase.perfomance) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.spotless)
    alias(libs.plugins.secrets) apply false
}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}