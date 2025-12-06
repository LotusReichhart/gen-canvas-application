import com.lotusreichhart.gencanvas.GenCanvasBuildType

plugins {
    alias(libs.plugins.gencanvas.android.application)
    alias(libs.plugins.gencanvas.android.application.compose)
    alias(libs.plugins.gencanvas.android.application.flavors)
    alias(libs.plugins.gencanvas.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.lotusreichhart.gencanvas"

    defaultConfig {
        applicationId = "com.lotusreichhart.gencanvas"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = GenCanvasBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = providers.gradleProperty("minifyWithR8")
                .map(String::toBooleanStrict).getOrElse(true)
            applicationIdSuffix = GenCanvasBuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.ui)

    implementation(projects.feature.onboarding)
    implementation(projects.feature.home)
    implementation(projects.feature.account)
    implementation(projects.feature.auth)
    implementation(projects.feature.studio)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.core.splashscreen)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}