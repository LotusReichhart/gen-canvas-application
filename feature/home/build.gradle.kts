plugins {
    alias(libs.plugins.gencanvas.android.feature)
}

android {
    namespace = "com.lotusreichhart.gencanvas.feature.home"
}

dependencies{
    implementation(libs.androidx.constraintlayout.compose)
}