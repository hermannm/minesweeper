plugins {
    // Applies the application plugin to add support for building a Java app.
    application

    // Applies OpenJFX plugin for JavaFX.
    id("org.openjfx.javafxplugin") version "0.0.12"
}

// Configures the JavaFX plugin.
javafx {
    version = "17.0.2"
    modules("javafx.controls")
}

repositories {
    // Uses Maven Central for resolving dependencies.
    mavenCentral()
}

// Reconfigures default source sets for simpler folder structure.
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
    // Defines the main class for the application.
    mainClass.set("dev.hermannm.minesweeper.Main")
}

tasks {
    // Configures the jar task to produce a fat jar with all dependencies.
    jar {
        dependsOn.addAll(listOf("compileJava", "processResources"))

        archiveBaseName.set("minesweeper")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        manifest { attributes(mapOf("Main-Class" to application.mainClass)) }

        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } + sourcesMain.output
        
        from(contents)
    }
}
