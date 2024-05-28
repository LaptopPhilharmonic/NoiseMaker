plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "uk.laptopphilharmonic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "uk.laptopphilharmonic.noisemaker.MainKt"
}