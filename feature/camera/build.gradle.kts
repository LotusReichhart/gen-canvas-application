plugins {
    alias(libs.plugins.gencanvas.android.feature)
}

android {
    namespace = "com.lotusreichhart.gencanvas.feature.camera"
}

dependencies {
    implementation(libs.camposer)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation("androidx.compose.runtime:runtime-livedata:1.7.5")
    implementation("com.github.SmartToolFactory:Compose-Colorful-Sliders:1.2.2")
}