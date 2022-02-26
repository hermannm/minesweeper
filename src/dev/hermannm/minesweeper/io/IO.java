package dev.hermannm.minesweeper.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Board;
import dev.hermannm.minesweeper.game.Game;

public class IO implements IOInterface {
	public void save(String filename, Game game) throws IOException {
		PrintWriter writer = new PrintWriter(filename);
		int bombCounter = game.getBombCounter();
		boolean[] gameStatus = { game.getGameOver(), game.getGameWon(), game.getFirstClick() };
		String firstLine = bombCounter + ",";
		for (boolean b : gameStatus) {
			firstLine += b + ",";
		}
		writer.println(firstLine.substring(0, firstLine.length() - 1));
		Board board = game.getBoard();
		int[] boardStatus = { board.getColumns(), board.getRows(), board.getNumberOfBombs() };
		String secondLine = "";
		for (int i : boardStatus) {
			secondLine += i + ",";
		}
		writer.println(secondLine.substring(0, secondLine.length() - 1));
		for (int y = 0; y < board.getRows(); y++) {
			for (int x = 0; x < board.getColumns(); x++) {
				Field field = board.getField(x, y);
				int adjacentBombs = field.getAdjacentBombs();
				boolean[] fieldStatus = { field.isBomb(), field.isHidden(), field.flagged() };
				String line = adjacentBombs + ",";
				for (boolean b : fieldStatus) {
					line += b + ",";
				}
				writer.println(line.substring(0, line.length() - 1));
			}
		}
		writer.flush();
		writer.close();
	}

	public Game load(String filename) throws IOException {
		Scanner scanner = new Scanner(new File(filename));
		String[] gameLine = scanner.nextLine().split(",");
		int bombCounter = Integer.parseInt(gameLine[0]);
		boolean gameOver = Boolean.parseBoolean(gameLine[1]);
		boolean gameWon = Boolean.parseBoolean(gameLine[2]);
		boolean firstClick = Boolean.parseBoolean(gameLine[3]);
		String[] boardLine = scanner.nextLine().split(",");
		int columns = Integer.parseInt(boardLine[0]);
		int rows = Integer.parseInt(boardLine[1]);
		int numberOfBombs = Integer.parseInt(boardLine[2]);
		Field[][] grid = new Field[columns][rows];
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				String[] fieldLine = scanner.nextLine().split(",");
				int adjacentBombs = Integer.parseInt(fieldLine[0]);
				boolean bomb = Boolean.parseBoolean(fieldLine[1]);
				boolean hidden = Boolean.parseBoolean(fieldLine[2]);
				boolean flag = Boolean.parseBoolean(fieldLine[3]);
				grid[x][y] = new Field(adjacentBombs, bomb, hidden, flag);
			}
		}
		scanner.close();
		Board board = new Board(grid, columns, rows, numberOfBombs);
		Game game = new Game(board, bombCounter, gameOver, gameWon, firstClick);
		return game;
	}
}
