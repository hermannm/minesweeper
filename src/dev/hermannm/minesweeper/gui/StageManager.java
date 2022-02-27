package dev.hermannm.minesweeper.gui;

import dev.hermannm.minesweeper.Controller;
import dev.hermannm.minesweeper.game.Board;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/** Manages the JavaFX stage, and which scene is set on it. */
public class StageManager {
	private Controller controller;
	private Stage stage;
	private MainMenu menu;
	private PlayMode playMode;

	public StageManager(Stage stage, Controller controller) {
		this.controller = controller;
		this.menu = new MainMenu(this.controller);
		this.stage = stage;
		stage.setTitle(Constants.GAME_NAME);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/minesweeper.png")));
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
		playMode = new PlayMode(this.controller, board, gameWon, gameOver, bombCounter);
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

	public void handleError(String errorMsg) {
		Alert error = new Alert(AlertType.ERROR);
		error.setContentText("Previous save file could not be found.");
		error.showAndWait();
	}
}
