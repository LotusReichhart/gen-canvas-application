plugins {
    alias(libs.plugins.gencanvas.android.library)
    alias(libs.plugins.gencanvas.android.library.compose)
}

android {
    namespace = "com.lotusreichhart.gencanvas.core.ui"
}

dependencies {
    api(projects.core.model)
    api(projects.core.common)
    api(projects.core.domain)

    api(libs.coil.compose)
    api(libs.coil.network.okhttp)
    api(libs.compose.sparkle.border)

    implementation(libs.javax.inject)

    implementation(libs.androidx.navigation.compose)
}