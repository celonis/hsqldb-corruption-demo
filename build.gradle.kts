import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL;
import org.gradle.api.tasks.testing.logging.TestLogEvent.*;

plugins {
    application
    id("org.springframework.boot") version "2.5.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

repositories {
    mavenCentral()
}

val hibernateVersion = "5.5.8.Final";

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.hibernate:hibernate-core:$hibernateVersion")

    implementation("org.hsqldb:hsqldb:2.6.1:debug")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

//application {
//    mainClass.set("org.hsqldb.App")
//}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        // set options for log level LIFECYCLE
        events = setOf(PASSED, SKIPPED, FAILED)
        displayGranularity = 2
        exceptionFormat = FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true

        maxGranularity = 3
        minGranularity = 0
    }
}

