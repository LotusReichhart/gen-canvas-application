plugins {
    alias(libs.plugins.gencanvas.android.library)
    alias(libs.plugins.gencanvas.android.library.compose)
}

android {
    namespace = "com.lotusreichhart.gencanvas.core.common"
}

dependencies {
    implementation(libs.javax.inject)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
}