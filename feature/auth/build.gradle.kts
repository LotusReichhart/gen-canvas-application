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
val googleClientIdPrd = properties.getProperty("GOOGLE_CLIENT_ID_PRD") ?: ""
val googleClientIdDev = properties.getProperty("GOOGLE_CLIENT_ID_DEV") ?: ""

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
            BuildConfigField("String", "\"$googleClientIdDev\"", "Google Client Id")
        )
    }

    onVariants(selector().withFlavor("environment" to "prod")) {
        it.buildConfigFields?.put(
            "GOOGLE_CLIENT_ID",
            BuildConfigField("String", "\"$googleClientIdPrd\"", "Google Client Id")
        )
    }
}

dependencies{
    implementation(libs.androidx.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
}