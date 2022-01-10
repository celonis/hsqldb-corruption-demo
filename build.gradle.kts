plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.hsqldb:hsqldb:2.6.1:debug")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

}

application {
    mainClass.set("org.hsqldb.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
