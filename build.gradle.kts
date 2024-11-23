
plugins {
    kotlin("jvm") version "2.0.21"
}

repositories{
    mavenCentral()
}


dependencies {

    implementation("org.xerial:sqlite-jdbc:3.46.1.3")
    implementation(kotlin("test"))
}