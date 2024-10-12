plugins {
    java
    jacoco
}

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("integrationTest")
    }
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.register<Test>("integrationTest") {
    group = "verification"
    description = "Runs the integration tests."
    useJUnitPlatform {
        include("integrationTest")
    }
}

// Do not generate reports for individual projects
tasks.jacocoTestReport {
    enabled = false
}
