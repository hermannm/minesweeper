package dev.hermannm.minesweeper;

import dev.hermannm.minesweeper.gui.GUI;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    GUI gui;

    public App() {
        gui = new GUI();
    }

    public void start(Stage stage) {
        stage.setTitle("Minesweeper");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/minesweeper.png")));
        gui.setStage(stage);
        stage.setScene(gui.menu());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
