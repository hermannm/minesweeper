plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application

    // Apply OpenJFX plugin for JavaFX.
    id("org.openjfx.javafxplugin") version "0.0.12"
}

// Configure JavaFX plugin.
javafx {
    version = "17.0.2"
    modules("javafx.controls")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1.1-jre")
}

// Reconfigure default source sets for simpler folder structure.
sourceSets {
    main {
        java {
            srcDirs("src")
        }
        resources {
            srcDirs("resources")
        }
    }
}

application {
    // Define the main class for the application.
    mainClass.set("dev.hermannm.minesweeper.App")
}
