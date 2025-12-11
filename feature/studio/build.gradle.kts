plugins {
    alias(libs.plugins.gencanvas.android.feature)
}

android {
    namespace = "com.lotusreichhart.gencanvas.feature.studio"
}

dependencies {
    implementation(libs.android.image.cropper)
    implementation(libs.photoeditor)
}