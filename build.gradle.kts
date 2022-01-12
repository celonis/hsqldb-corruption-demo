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
    implementation("org.hibernate:hibernate-c3p0:$hibernateVersion")
    implementation("org.hibernate:hibernate-ehcache:$hibernateVersion")

    implementation("org.hsqldb:hsqldb:2.6.1:debug")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

application {
    mainClass.set("org.hsqldb.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
