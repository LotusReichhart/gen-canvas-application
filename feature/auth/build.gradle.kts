import com.android.build.api.variant.BuildConfigField
import java.util.Properties

plugins {
    alias(libs.plugins.gencanvas.android.feature)
}

val properties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream())
}
val googleClientId = properties.getProperty("GOOGLE_CLIENT_ID") ?: ""

android {
    namespace = "com.lotusreichhart.gencanvas.feature.auth"

    buildFeatures {
        buildConfig = true
    }
}

androidComponents {
    onVariants(selector().withFlavor("environment" to "dev")) {
        it.buildConfigFields?.put(
            "GOOGLE_CLIENT_ID",
            BuildConfigField("String", "\"$googleClientId\"", "Google Client Id")
        )
    }

    onVariants(selector().withFlavor("environment" to "prod")) {
        it.buildConfigFields?.put(
            "GOOGLE_CLIENT_ID",
            BuildConfigField("String", "\"$googleClientId\"", "Google Client Id")
        )
    }
}

dependencies{
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
}