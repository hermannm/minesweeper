package dev.hermannm.minesweeper;

import java.io.IOException;

import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Board;
import dev.hermannm.minesweeper.game.Game;
import dev.hermannm.minesweeper.gui.StageManager;
import dev.hermannm.minesweeper.io.SaveFileHandler;
import javafx.stage.Stage;

/** Serves as a middle man between the game, the GUI and IO handling. */
public class Controller {
	Game game;
	StageManager stageManager;
	SaveFileHandler saveHandler;

	public Controller(Stage stage) {
		this.saveHandler = new SaveFileHandler();
		this.stageManager = new StageManager(stage, this);
	}

	/** Calls on the stage manager to show the main menu. */
	public void showMenu() {
		stageManager.updateScene(stageManager.getMenuScene());
	}

	/** Calls on the stage manager to reflect new changes on the board. */
	public void updateBoard() {
		stageManager.updatePlayMode(game.getGameWon(), game.getGameOver(), game.getBombCounter());
	}

	/** Calls on the game to handle the clicked field, and then updates the view. */
	public void handleClick(Field field) {
		game.clickField(field);
		updateBoard();
	}

	/** Calls on the game handle the flagged field, and then updates the view. */
	public void handleFlag(Field field) {
		game.flagField(field);
		updateBoard();
	}

	/** Calls on the game to handle the click, and then updates the view. */
	public void handleRevealAdjacent(Field field) {
		game.revealAdjacentFields(field);
		updateBoard();
	}

	/**
	 * Initializes a new game with the given options,
	 * and calls on the game manager to show the game.
	 */
	public void newGame(int columns, int rows, int numberOfBombs) {
		game = new Game(new Board(columns, rows, numberOfBombs));
		stageManager.initializePlayMode(game.getBoard(), game.getGameWon(), game.getGameOver(), game.getBombCounter());
	}

	/**
	 * Calls on the IO handler to save the game,
	 * and shows an error if it failed.
	 */
	public void save() {
		try {
			saveHandler.save("minesweeper_save.txt", game);
		} catch (IOException e) {
			stageManager.handleError("File could not be saved.");
		}
	}

	/**
	 * Calls on the IO handler to load a game save file,
	 * and shows an error if it failed.
	 */
	public void load() {
		try {
			game = saveHandler.load("minesweeper_save.txt");
			updateBoard();
		} catch (IOException e) {
			stageManager.handleError("Save file could not be found.");
		}
	}
}
