package dev.hermannm.minesweeper;

public class GameOption {
	private String label;
	private int columns;
	private int rows;
	private int numberOfBombs;

	/** Configuration of the game mode buttons in the main menu. */
	public static final GameOption[] OPTIONS = {
			new GameOption("Easy", 8, 8, 10),
			new GameOption("Normal", 16, 16, 40),
			new GameOption("Hard", 30, 16, 99),
			new GameOption("Insanity", 30, 16, 470)
	};

	public GameOption(String label, int columns, int rows, int numberOfBombs) {
		this.label = label;
		this.columns = columns;
		this.rows = rows;
		this.numberOfBombs = numberOfBombs;
	}

	public String getLabel() {
		return label;
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
}
