package com.maemresen.fintrack.gradle.plugin

plugins {
    java
}


tasks.withType<Jar>().configureEach {
    archiveBaseName.set("fintrack-${project.name}")
}

extensions.configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21 // Replace with your desired version
}

tasks.withType<Test>() {
    useJUnitPlatform {
        filter {
            includeTestsMatching("*Test")
            excludeTestsMatching("*IT")
            isFailOnNoMatchingTests = false
        }
        failFast = false
    }
}

val integrationTest = tasks.register<Test>("integrationTest") {
    useJUnitPlatform {
        filter {
            includeTestsMatching("*IT")
            isFailOnNoMatchingTests = false
        }
    }
}

tasks.named("integrationTest") {
    dependsOn("test")
}

tasks.named("check") {
    dependsOn(integrationTest)
}