import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    javaApp
    `java-library`
    springApp
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

val mapstructVersion = "1.5.5.Final"

dependencies {
    api(project(":app:shared:persistence"))
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.apache.commons:commons-collections4:4.4")

    implementation("org.mapstruct:mapstruct:${mapstructVersion}")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation(project(":app:lib:spring-test"))
}
