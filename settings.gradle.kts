pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GenCanvas"
include(":app")
include(":domain")
include(":data")
include(":auth")
include(":auth")
include(":features")
include(":features:auth")
include(":features:home")
include(":core")
include(":features:onboarding")
include(":features:ads")
include(":ads")
include(":features:user")
