import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    `jacoco-report-aggregation`
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependencyManagement)
}

tasks.jar {
    enabled = false
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    jacocoAggregation(project(":app:persistence"))
    jacocoAggregation(project(":app:business-logic"))
    jacocoAggregation(project(":app:core"))
    jacocoAggregation(project(":app:boot:api"))
    jacocoAggregation(project(":app:boot:webapp"))
}

subprojects {
    if (childProjects.isEmpty()) {
        plugins.withType<JavaPlugin> {
            apply(plugin = "jacoco")

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
                    includeTags("integrationTest")
                }
            }

            tasks.jacocoTestReport {
                enabled = false
            }
        }
    }
}