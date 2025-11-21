import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}

val rewardedAdUnitIdDev = properties.getProperty("REWARDED_AD_UNIT_ID_DEV") ?: ""
val rewardedAdUnitIdProd = properties.getProperty("REWARDED_AD_UNIT_ID_PROD") ?: ""

android {
    namespace = "com.lotusreichhart.ads"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField(
                "String",
                "REWARDED_AD_UNIT_ID",
                "\"$rewardedAdUnitIdDev\""
            )
        }

        create("prod") {
            dimension = "environment"
            buildConfigField(
                "String",
                "REWARDED_AD_UNIT_ID",
                "\"$rewardedAdUnitIdProd\""
            )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Module
    implementation(project(":core"))
    implementation(project(":domain"))

    // Google Service Ads
    implementation("com.google.android.gms:play-services-ads:24.4.0")

    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")
}