package dev.hermannm.minesweeper.game;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores the grid of fields on the Minesweeper board,
 * and provides utility methods for setting up and processing the board.
 */
public class Board {
	private Field[][] grid;
	private int columns;
	private int rows;
	private int numberOfBombs;

	/**
	 * Instantiates a board with the given dimensions and number of bombs,
	 * and distributes the bombs randomly.
	 */
	public Board(int columns, int rows, int numberOfBombs) {
		if (columns < 1 || rows < 1 || numberOfBombs < 0 || numberOfBombs > columns * rows - 8) {
			throw new IllegalArgumentException("Invalid board.");
		} else {
			this.columns = columns;
			this.rows = rows;
			this.numberOfBombs = numberOfBombs;
			this.grid = new Field[columns][rows];

			fillBoard();
			plantBombs();
			updateAdjacentBombs();
		}
	}

	/** Instantiates a board from the pre-defined set of fields. */
	public Board(Field[][] grid, int columns, int rows, int numberOfBombs) {
		if (columns < 1 || rows < 1 || numberOfBombs < 0 || numberOfBombs > columns * rows - 8) {
			throw new IllegalArgumentException("Invalid board.");
		} else {
			this.columns = columns;
			this.rows = rows;
			this.numberOfBombs = numberOfBombs;
			this.grid = grid;
		}
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public int getNumberOfBombs() {
		return numberOfBombs;
	}

	/** Returns the field from the given position on the board. */
	public Field getField(int x, int y) {
		if (x < 0 || y < 0 || x >= columns || y >= rows) {
			throw new ArrayIndexOutOfBoundsException("X/Y board coordinates out of bounds.");
		} else {
			return grid[x][y];
		}
	}

	/** Returns a list of fields adjacent to the given field. */
	public List<Field> getAdjacentFields(Field field) {
		List<Field> adjacentFields = new ArrayList<>();

		// Adds the field's neighbors to the list.
		// Makes sure that the board's edges are respected.
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				if (getField(x, y) == field) {
					if (x >= 1) {
						adjacentFields.add(getField(x - 1, y));
					}
					if (y >= 1) {
						adjacentFields.add(getField(x, y - 1));
					}
					if (x < columns - 1) {
						adjacentFields.add(getField(x + 1, y));
					}
					if (y < rows - 1) {
						adjacentFields.add(getField(x, y + 1));
					}
					if (x >= 1 && y >= 1) {
						adjacentFields.add(getField(x - 1, y - 1));
					}
					if (x < columns - 1 && y >= 1) {
						adjacentFields.add(getField(x + 1, y - 1));
					}
					if (x < columns - 1 && y < rows - 1) {
						adjacentFields.add(getField(x + 1, y + 1));
					}
					if (x >= 1 && y < rows - 1) {
						adjacentFields.add(getField(x - 1, y + 1));
					}
				}
			}
		}

		return adjacentFields;
	}

	/** Fills the board with default fields. */
	public void fillBoard() {
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				grid[x][y] = new Field();
			}
		}
	}

	/** Sets the appropriate number of random fields as bombs. */
	public void plantBombs() {
		List<int[]> allCoords = new ArrayList<>();

		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				allCoords.add(new int[] { x, y });
			}
		}

		for (int i = 0; i < numberOfBombs; i++) {
			int randomIndex = (int) Math.floor(Math.random() * allCoords.size());
			int[] randomCoord = allCoords.get(randomIndex);

			Field field = this.getField(randomCoord[0], randomCoord[1]);
			field.setBomb();

			allCoords.remove(randomCoord);
		}
	}

	/** Goes through the board and sets the adjacent bombs value for each field. */
	public void updateAdjacentBombs() {
		Arrays.stream(grid)
				.flatMap(Arrays::stream)
				.forEach(field -> field.setAdjacentBombs(
						getAdjacentFields(field).stream()
								.filter(Field::isBomb)
								.mapToInt(f -> 1)
								.sum()));
	}

	/** Returns a list of fields on the board that are not bombs. */
	public List<Field> getEmptyFields() {
		return Arrays.stream(grid)
				.flatMap(Arrays::stream)
				.filter(field -> !field.isBomb())
				.collect(Collectors.toList());
	}

	/**
	 * Moves bombs in the given field or adjacent fields
	 * to other random fields on the board.
	 */
	public void moveBomb(Field field) {
		List<Field> emptyFields = getEmptyFields();

		int bombsToMove = 0;

		List<Field> fieldWithAdjacent = getAdjacentFields(field);
		fieldWithAdjacent.add(field);

		for (Field f : fieldWithAdjacent) {
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

		updateAdjacentBombs();
	}
}
