pluginManagement {
    repositories {
        google()
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

rootProject.name = "Fintrack"

include(":modules:fintrack-webapp-ui")
include(":modules:fintrack-persistence")
include(":modules:fintrack-business")
include(":modules:commons:fintrack-spring-test")