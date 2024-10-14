import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

dependencies {
    implementation(project(":app:core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.springdoc.ui)

    testImplementation(project(":app:util:test-util"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<BootRun> {
    environment("SPRING_PROFILES_ACTIVE", "local")
    environment("SPRING_DOCKER_COMPOSE_FILE", "${rootProject.projectDir}/docker/docker-compose.yaml")
}