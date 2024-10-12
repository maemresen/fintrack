import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    fintrack.`java-conventions`
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

tasks.withType<BootRun> {
    environment("SPRING_PROFILES_ACTIVE", "local")
    environment("SPRING_DOCKER_COMPOSE_FILE", "${rootProject.projectDir}/docker/docker-compose.yaml")
}