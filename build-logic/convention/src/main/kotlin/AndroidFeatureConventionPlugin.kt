import com.android.build.api.dsl.LibraryExtension
import com.lotusreichhart.gencanvas.configureAndroidCompose
import com.lotusreichhart.gencanvas.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "gencanvas.android.library")
            apply(plugin = "gencanvas.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
                configureAndroidCompose(this)
            }

            dependencies {
                "implementation"(project(":core:ui"))

                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.navigation.compose").get())

                "testImplementation"(libs.findLibrary("androidx.navigation.testing").get())
            }
        }
    }
}