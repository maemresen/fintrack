plugins {
    id("java")
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":modules:fintrack-business"))
    testImplementation(project(":modules:commons:fintrack-spring-test"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

