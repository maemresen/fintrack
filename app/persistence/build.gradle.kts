import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    fintrack.javaBase
    `java-library`
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependencyManagement)
}

dependencyManagement {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}
