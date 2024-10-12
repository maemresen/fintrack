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
    jacocoAggregation(project(":app:persistence"))
    jacocoAggregation(project(":app:business-logic"))
    jacocoAggregation(project(":app:core"))
    jacocoAggregation(project(":app:boot:api"))
    jacocoAggregation(project(":app:boot:webapp"))
}
