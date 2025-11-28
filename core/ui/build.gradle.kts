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

    api("io.coil-kt.coil3:coil-compose:3.1.0")
    api("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    api("com.github.LotusReichhart:compose-sparkle-border:1.0.1")
}