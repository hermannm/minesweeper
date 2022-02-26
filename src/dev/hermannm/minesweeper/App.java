package dev.hermannm.minesweeper;

import dev.hermannm.minesweeper.gui.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

/** Entry point of the application. */
public class App extends Application {
    GUI gui;

    public void start(Stage stage) {
        gui = new GUI(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
