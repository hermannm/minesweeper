package dev.hermannm.minesweeper.gui;

import dev.hermannm.minesweeper.Controller;
import dev.hermannm.minesweeper.game.Board;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI {
	private Controller controller;
	private Stage stage;
	private MainMenu menu;
	private PlayMode playMode;

	public GUI(Stage stage, Controller controller) {
		this.controller = controller;
		this.menu = new MainMenu(this.controller);
		this.stage = stage;
		stage.setTitle(Constants.GAME_NAME);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/minesweeper.png")));
		stage.setScene(this.getMenuScene());
		stage.show();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setResizable(false);
	}

	public void updateScene(Scene scene) {
		stage.setScene(scene);
	}

	public Scene getMenuScene() {
		return menu.getScene();
	}

	public Scene getPlayModeScene() {
		return playMode.getScene();
	}

	public void initializePlayMode(Board board, boolean gameWon, boolean gameOver, int bombCounter) {
		playMode = new PlayMode(this.controller, board, gameWon, gameOver, bombCounter);
	}

	public void updatePlayMode(boolean gameWon, boolean gameOver, int bombCounter) {
		playMode.updateGrid();
		playMode.updateMenuButton(gameWon, gameOver);
		playMode.updateBombCounter(bombCounter);
	}
}
