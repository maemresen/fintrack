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

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

