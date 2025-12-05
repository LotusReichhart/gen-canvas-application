plugins {
    alias(libs.plugins.gencanvas.android.feature)
}

android {
    namespace = "com.lotusreichhart.gencanvas.feature.editing"
}

dependencies {
    implementation(libs.android.image.cropper)
}