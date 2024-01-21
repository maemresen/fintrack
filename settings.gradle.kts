pluginManagement {
    plugins {
        id("org.springframework.boot") version "3.2.1"
        id("io.spring.dependency-management") version "1.1.4"
    }
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

include(":app:lib:spring-test")
include(":app:shared:persistence")
include(":app:shared:business")
include(":app:webapp-api")