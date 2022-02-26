package dev.hermannm.minesweeper;

import java.io.IOException;

import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Board;
import dev.hermannm.minesweeper.game.Game;
import dev.hermannm.minesweeper.gui.StageManager;
import dev.hermannm.minesweeper.io.IO;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller {
	Game game;
	StageManager stageManager;
	IO io;

	public Controller(Stage stage) {
		this.io = new IO();
		this.stageManager = new StageManager(stage, this);
	}

	public void showMenu() {
		stageManager.updateScene(stageManager.getMenuScene());
	}

	public void updateBoard() {
		stageManager.updatePlayMode(game.getGameWon(), game.getGameOver(), game.getBombCounter());
	}

	public void handleClick(Field field) {
		game.clickField(field);
		updateBoard();
	}

	public void handleFlag(Field field) {
		game.flagField(field);
		updateBoard();
	}

	public void handleRevealAdjacent(Field field) {
		game.revealAdjacentFields(field);
		updateBoard();
	}

	public void newGame(int columns, int rows, int numberOfBombs) {
		game = new Game(new Board(columns, rows, numberOfBombs));
		stageManager.initializePlayMode(game.getBoard(), game.getGameWon(), game.getGameOver(), game.getBombCounter());
	}

	public void save() {
		try {
			io.save("minesweeperSave.txt", game);
		} catch (IOException e) {
		}
	}

	public void load() {
		try {
			game = io.load("minesweeperSave.txt");
			updateBoard();
		} catch (IOException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setContentText("404 - file not found");
			error.showAndWait();
		}
	}
}
