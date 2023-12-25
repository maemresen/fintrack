plugins {
    id("java")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":app:shared:business"))
    testImplementation(project(":app:lib:spring-test"))
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

