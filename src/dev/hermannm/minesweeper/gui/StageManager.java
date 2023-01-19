package dev.hermannm.minesweeper.gui;

import dev.hermannm.minesweeper.Controller;
import dev.hermannm.minesweeper.game.Board;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.InputStream;

/** Manages the JavaFX stage, and which scene is set on it. */
public class StageManager {
    private final Controller controller;
    private final Stage stage;
    private MainMenu menu;
    private PlayMode playMode;

    public StageManager(Stage stage, Controller controller) {
        this.controller = controller;
        this.stage = stage;

        try {
            this.menu = new MainMenu(this.controller);
        } catch (FileNotFoundException exception) {
            StageManager.showError(exception.getMessage());
        }

        try (InputStream icon = getClass().getResourceAsStream("/img/minesweeper.png")) {
            assert icon != null;
            stage.getIcons().add(new Image(icon));
        } catch (Exception ignored) {
            StageManager.showError("Minesweeper icon not found.");
        }

        stage.setTitle(Constants.GAME_NAME);
        stage.setScene(this.getMenuScene());
        stage.show();
    }

    /** Updates the stage's scene. */
    public void updateScene(Scene scene) {
        stage.setScene(scene);
    }

    /** Returns the scene for the main menu. */
    public Scene getMenuScene() {
        return menu.getScene();
    }

    /** Returns the scene for the game's active play mode. */
    public Scene getPlayModeScene() {
        return playMode.getScene();
    }

    /**
     * Initializes the active play mode with the given values,
     * then updates the scene to the play mode.
     */
    public void initializePlayMode(Board board, boolean gameWon, boolean gameOver, int bombCounter) {
        try {
            playMode = new PlayMode(this.controller, board, gameWon, gameOver, bombCounter);
        } catch (FileNotFoundException exception) {
            StageManager.showError(exception.getMessage());
        }

        updateScene(getPlayModeScene());
    }

    /**
     * Updates the play mode scene with the given values,
     * and also updates the grid to reflect changes on the board.
     */
    public void updatePlayMode(Board board, boolean gameWon, boolean gameOver, int bombCounter) {
        playMode.updateGrid(board);
        playMode.updateMenuButton(gameWon, gameOver);
        playMode.updateBombCounter(bombCounter);
    }

    /**
     * Shows a dialog with the given error message to the user.
     */
    public static void showError(String errorMessage) {
        Alert error = new Alert(AlertType.ERROR);
        error.setContentText(errorMessage);
        error.showAndWait();
    }
}
