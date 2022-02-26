package dev.hermannm.minesweeper;

import javafx.application.Application;
import javafx.stage.Stage;

/** Entry point of the application. */
public class App extends Application {
    Controller controller;

    public void start(Stage stage) {
        controller = new Controller(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
