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

rootProject.name = "Upcoming Sports Events"

include(":app")

include(":domain:sport")

include(":data:sport")
include(":data:sport:datasource")

include(":presentation:eventitem")
include(":presentation:sportitem")
include(":presentation:home")

include(":presentation:theme")
