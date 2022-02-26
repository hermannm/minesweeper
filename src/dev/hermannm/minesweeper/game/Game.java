package dev.hermannm.minesweeper.game;

import java.util.List;

public class Game {
	Board board;
	private int bombCounter;
	private boolean gameOver = false, gameWon = false, firstClick = true;

	public Game(Board board) {
		this.board = board;
		bombCounter = board.getNumberOfBombs();
	}

	public Game(Board board, int bombCounter, boolean gameOver, boolean gameWon, boolean firstClick) {
		this.board = board;
		this.bombCounter = bombCounter;
		this.gameOver = gameOver;
		this.gameWon = gameWon;
		this.firstClick = firstClick;
	}

	public Board getBoard() {
		return board;
	}

	public int getBombCounter() {
		return bombCounter;
	}

	public boolean getGameOver() {
		return gameOver;
	}

	public boolean getGameWon() {
		return gameWon;
	}

	public boolean getFirstClick() {
		return firstClick;
	}

	public void clickField(Field field) {
		List<Field> adjacentFields = board.getAdjacentFields(field);

		if (!field.isHidden() || field.flagged() || gameOver || gameWon) {
			return;
		}

		if (firstClick) {
			if (field.isBomb() || !(field.getAdjacentBombs() == 0)) {
				this.moveBomb(field);
			}
			firstClick = false;
		}

		if (field.isBomb()) {
			this.gameOver();
		} else {
			field.reveal();
			if (field.getAdjacentBombs() == 0) {
				for (Field f : adjacentFields) {
					this.clickField(f);
				}
			}
		}

		this.checkWin();
	}

	public void flagField(Field field) {
		if (!field.isHidden() || gameOver || gameWon) {
			return;
		}

		if (field.flagged()) {
			field.removeFlag();
			bombCounter++;
		} else {
			field.setFlag();
			bombCounter--;
		}
	}

	public void revealAdjacentFields(Field field) {
		List<Field> adjacentFields = board.getAdjacentFields(field);

		if (gameOver || gameWon) {
			return;
		}

		int adjacentFlags = 0;
		for (Field f : adjacentFields) {
			if (f.flagged()) {
				adjacentFlags++;
			}
		}

		if (field.getAdjacentBombs() == adjacentFlags) {
			for (Field f : adjacentFields) {
				this.clickField(f);
			}
		}
	}

	public void gameOver() {
		gameOver = true;

		for (int y = 0; y < board.getRows(); y++) {
			for (int x = 0; x < board.getColumns(); x++) {
				Field field = board.getField(x, y);
				if (field.isBomb()) {
					field.reveal();
				}
			}
		}

		bombCounter = board.getNumberOfBombs();
	}

	public void checkWin() {
		if (gameOver) {
			return;
		}

		for (int y = 0; y < board.getRows(); y++) {
			for (int x = 0; x < board.getColumns(); x++) {
				Field field = board.getField(x, y);
				if (!field.isBomb() && field.isHidden()) {
					return;
				}
			}
		}

		for (int y = 0; y < board.getRows(); y++) {
			for (int x = 0; x < board.getColumns(); x++) {
				Field field = board.getField(x, y);
				if (!field.flagged()) {
					this.flagField(field);
				}
			}
		}

		gameWon = true;
	}

	public void moveBomb(Field field) {
		List<Field> adjacentFields = board.getAdjacentFields(field);
		List<Field> emptyFields = board.getEmptyFields();
		int bombsToMove = 0;

		if (field.isBomb()) {
			field.removeBomb();
			bombsToMove++;
		} else {
			emptyFields.remove(field);
		}

		for (Field f : adjacentFields) {
			if (f.isBomb()) {
				f.removeBomb();
				bombsToMove++;
			} else {
				emptyFields.remove(f);
			}
		}

		for (int i = 0; i < bombsToMove; i++) {
			int randomIndex = (int) Math.floor(Math.random() * emptyFields.size());
			Field randomEmptyField = emptyFields.get(randomIndex);
			randomEmptyField.setBomb();
			emptyFields.remove(randomEmptyField);
		}

		board.checkAdjacentBombs();
	}
}
