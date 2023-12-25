import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    javaApp
    `java-library`
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}
dependencies {
    api("org.springframework.boot:spring-boot-starter-test")
    implementation("org.testcontainers:postgresql")
    implementation("org.testcontainers:junit-jupiter")
    implementation("org.junit.jupiter:junit-jupiter-params")
    implementation("io.rest-assured:rest-assured")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("org.springframework:spring-web")
    implementation("jakarta.validation:jakarta.validation-api")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}
