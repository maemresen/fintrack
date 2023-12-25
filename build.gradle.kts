buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

group = "com.maemresen"
version = "0.0.1-SNAPSHOT"

subprojects {
    tasks.withType<Jar> {
        archiveBaseName = "fintrack-${project.name}-${project.version}";
    }
}