plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")

}

application {
    mainClass.set("org.hsqldb.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
