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

dependencies {
    api(project(":app:persistence"))

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}
