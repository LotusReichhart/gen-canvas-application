plugins {
    alias(libs.plugins.gencanvas.android.library)
}

android {
    namespace = "com.lotusreichhart.gencanvas.core.common"
}

dependencies {
    implementation(libs.javax.inject)
}