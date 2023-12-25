import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    `java-library`
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("org.projectlombok:lombok")
    testImplementation(project(":app:lib:spring-test"))

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

