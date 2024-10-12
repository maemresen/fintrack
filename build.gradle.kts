import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    java
    `jacoco-report-aggregation`
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependencyManagement)
}

repositories{
    mavenCentral()
    mavenLocal()
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    jacocoAggregation(project(":app:api"))
    jacocoAggregation(project(":app:webapp"))
}
