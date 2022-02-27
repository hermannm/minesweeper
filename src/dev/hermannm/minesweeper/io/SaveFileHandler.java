package dev.hermannm.minesweeper.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Board;
import dev.hermannm.minesweeper.game.Game;

/** Implements saving and loading games to and from text files. */
public class SaveFileHandler implements GameSaver, GameLoader {
	/** Saves the given game to a file of the given name. */
	public void save(Game game, String filename) throws IOException {
		PrintWriter writer = new PrintWriter(filename);

		// Writes the first line with the game status.
		String firstLine = game.getBombCounter() + ",";
		boolean[] gameStatus = { game.getGameOver(), game.getGameWon(), game.getFirstClick() };
		for (boolean statusField : gameStatus) {
			firstLine += statusField + ",";
		}
		firstLine = firstLine.substring(0, firstLine.length() - 1);
		writer.println(firstLine);

		// Writes the second line with the board config.
		String secondLine = "";
		Board board = game.getBoard();
		int[] boardConfig = { board.getColumns(), board.getRows(), board.getNumberOfBombs() };
		for (int configField : boardConfig) {
			secondLine += configField + ",";
		}
		secondLine = secondLine.substring(0, secondLine.length() - 1);
		writer.println(secondLine);

		// Writes a line for each field on the board.
		for (int x = 0; x < board.getColumns(); x++) {
			for (int y = 0; y < board.getRows(); y++) {
				Field field = board.getField(x, y);

				String fieldLine = field.getAdjacentBombs() + ",";

				boolean[] fieldStatus = { field.isBomb(), field.isHidden(), field.flagged() };
				for (boolean statusField : fieldStatus) {
					fieldLine += statusField + ",";
				}

				fieldLine = fieldLine.substring(0, fieldLine.length() - 1);
				writer.println(fieldLine);
			}
		}

		writer.flush();
		writer.close();
	}

	public Game load(String filename) throws IOException {
		Scanner scanner = new Scanner(new File(filename));

		// Parses the first line with the game status.
		String[] gameLine = scanner.nextLine().split(",");
		int bombCounter = Integer.parseInt(gameLine[0]);
		boolean gameOver = Boolean.parseBoolean(gameLine[1]);
		boolean gameWon = Boolean.parseBoolean(gameLine[2]);
		boolean firstClick = Boolean.parseBoolean(gameLine[3]);

		// Parses the second line with the board config.
		String[] boardLine = scanner.nextLine().split(",");
		int columns = Integer.parseInt(boardLine[0]);
		int rows = Integer.parseInt(boardLine[1]);
		int numberOfBombs = Integer.parseInt(boardLine[2]);

		// Parses the remaining lines with the status for each field.
		Field[][] grid = new Field[columns][rows];
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				String[] fieldLine = scanner.nextLine().split(",");

				int adjacentBombs = Integer.parseInt(fieldLine[0]);
				boolean bomb = Boolean.parseBoolean(fieldLine[1]);
				boolean hidden = Boolean.parseBoolean(fieldLine[2]);
				boolean flag = Boolean.parseBoolean(fieldLine[3]);

				grid[x][y] = new Field(adjacentBombs, bomb, hidden, flag);
			}
		}

		scanner.close();

		// Constructs a new board and game from the parsed results.
		Board board = new Board(grid, columns, rows, numberOfBombs);
		Game game = new Game(board, bombCounter, gameOver, gameWon, firstClick);

		return game;
	}
}
