# Minesweeper

Java implementation of the classic Minesweeper game, using JavaFX for the graphics. Created as part of a [course on object-oriented programming](https://www.ntnu.edu/studies/courses/TDT4100#tab=omEmnet) at NTNU Trondheim, spring 2019. Updated for readability and distributability spring 2022.

**Table of Contents**

- [Screenshots](#screenshots)
- [Running the App](#running-the-app)
- [Project Structure](#project-structure)
- [Credits](#credits)

## Screenshots

<p align="center">
  <img align=middle alt="Menu screenshot" title="Menu screenshot" src="https://github.com/hermannm/minesweeper/blob/assets/menu_screenshot.png">
  <img align=middle alt="Gameplay screenshot" title="Gameplay screenshot" src="https://github.com/hermannm/minesweeper/blob/assets/gameplay_screenshot.png">
</p>

## Running the App

### Through the JAR

1. Download the latest `minesweeper.jar` from Releases (https://github.com/hermannm/minesweeper/releases)

2. Double click the downloaded `.jar` to run it!
   - Mac users may have to allow it through `System Preferences -> Security & Privacy`

You can also run the JAR from the terminal:

```
java -jar minesweeper.jar
```

### Through Gradle

1. Clone the repo

```
git clone https://github.com/hermannm/minesweeper.git
```

2. Navigate to the new folder

```
cd minesweeper
```

3. Build the project

```
./gradlew build
```

4. Run the app

```
./gradlew run
```

## Project Structure

- `src` contains the Java packages for the project.
  - Package `dev.hermannm.minesweeper` contains the entry point of the application, as well as its controller and game mode configuration.
    - Subpackage `game` contains the game logic for Minesweeper.
    - Subpackage `gui` contains the JavaFX graphical user interface for the game.
    - Subpackage `io` contains a class for saving and loading games to and from text files.
- `resources` contains the static assets for the app.
  - `styles.css` is the JavaFX CSS stylesheet for the UI.
  - `fonts` contains the font files used in the stylesheet.
  - `img` contains the Minesweeper icons used in the stylesheet and the game.
- `build.gradle.kts` contains the Gradle build config for the project.

## Credits

- The team behind [OpenJFX](https://github.com/openjdk/jfx)
- [Gezoda](https://fontstruct.com/fontstructors/593973/gezoda) for the wonderful [Minesweeper font](https://fontstruct.com/fontstructions/show/1501665/mine-sweeper)
- [Wikimedia](https://wikimediafoundation.org/) for hosting the [Minesweeper icons](https://commons.wikimedia.org/wiki/Category:Minesweeper)
