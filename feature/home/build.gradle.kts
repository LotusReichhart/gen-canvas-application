plugins {
    alias(libs.plugins.gencanvas.android.feature)
}

android {
    namespace = "com.lotusreichhart.gencanvas.feature.home"
}

dependencies{
    implementation(projects.core.data)

    implementation(libs.androidx.constraintlayout.compose)
}