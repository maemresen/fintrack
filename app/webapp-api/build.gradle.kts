plugins {
    javaApp
    springApp
}

val springDocVersion = "2.1.0"

dependencies {
    implementation(project(":app:shared:business"))
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${springDocVersion}")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation(project(":app:lib:spring-test"))

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
}
