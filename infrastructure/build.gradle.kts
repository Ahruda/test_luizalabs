plugins {
    java
    `java-conventions`
    `jacoco-report-aggregation`
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.luizalabs.orders.infrastructure"

tasks.bootJar {
    archiveBaseName.set("app")
    destinationDirectory.set(file("$rootDir/build/libs"))

    dependsOn(tasks.named("test"))
    finalizedBy(tasks.named("jacocoTestReport"))
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.testCodeCoverageReport {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("$rootDir/build/reports/jacoco/test/"))
    }
}

tasks.named("jacocoTestReport") {
    dependsOn(tasks.named("testCodeCoverageReport"))
}
