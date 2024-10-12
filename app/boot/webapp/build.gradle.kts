import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    fintrack.javaBase
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

dependencies {
    implementation(project(":app:core"))
    implementation(project(":app:business-logic"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

tasks.withType<BootRun> {
    environment("SPRING_PROFILES_ACTIVE", "local")
    environment("SPRING_DOCKER_COMPOSE_FILE", "${rootProject.projectDir}/docker/docker-compose.yaml")
}