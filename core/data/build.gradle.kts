import com.android.build.api.variant.BuildConfigField
import java.util.Properties

plugins {
    alias(libs.plugins.gencanvas.android.library)
    alias(libs.plugins.gencanvas.android.room)
    alias(libs.plugins.gencanvas.hilt)
}

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
val baseUrlDev = properties.getProperty("BASE_URL_DEV") ?: "http://localhost:5000"
val baseUrlProd = properties.getProperty("BASE_URL_PROD") ?: "https://api.example.com"

android {
    namespace = "com.lotusreichhart.gencanvas.core.data"

    buildFeatures {
        buildConfig = true
    }
}

androidComponents {
    onVariants(selector().withFlavor("environment" to "dev")) {
        it.buildConfigFields?.put(
            "BASE_URL",
            BuildConfigField("String", "\"$baseUrlDev\"", "Dev Base URL")
        )
    }

    onVariants(selector().withFlavor("environment" to "prod")) {
        it.buildConfigFields?.put(
            "BASE_URL",
            BuildConfigField("String", "\"$baseUrlProd\"", "Prod Base URL")
        )
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.domain)

    implementation(libs.androidx.dataStore)
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}