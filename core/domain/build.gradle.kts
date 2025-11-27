plugins {
    alias(libs.plugins.gencanvas.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.lotusreichhart.gencanvas.core.domain"
}

dependencies {
    api(projects.core.model)

    implementation(libs.javax.inject)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}