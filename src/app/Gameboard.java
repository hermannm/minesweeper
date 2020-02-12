package app;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

public class Gameboard {
	private Field[][] grid;
	private int columns, rows, numberOfBombs;
	public Gameboard(int columns, int rows, int numberOfBombs) {
		if(columns < 1 || rows < 1 || numberOfBombs < 0 || numberOfBombs > columns*rows-8) {
			throw new IllegalArgumentException("Invalid gameboard.");
		}else {
			this.columns = columns;
			this.rows = rows;
			this.numberOfBombs = numberOfBombs;
			grid = new Field[columns][rows];
			fillBoard();
			plantBombs();
			checkAdjacentBombs();
		}
	}
	public Gameboard(Field[][] grid, int columns, int rows, int numberOfBombs) {
		if(columns < 1 || rows < 1 || numberOfBombs < 0 || numberOfBombs > columns*rows-8) {
			throw new IllegalArgumentException("Invalid gameboard.");
		}else {
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
	public Field getField(int x, int y) {
		if(x < 0 || y < 0 || x >= columns || y >= rows) {
			throw new IllegalArgumentException("X/Y-coordinates out of bounds.");
		}else {
			return grid[x][y];
		}
	}
	public List<Field> getAdjacentFields(Field field) {
		List<Field> adjacentFields = new ArrayList<>();
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				if(getField(x,y) == field) {
					if(x >= 1) {
						adjacentFields.add(getField(x-1,y));
					}
					if(y >= 1) {
						adjacentFields.add(getField(x,y-1));
					}
					if(x < columns-1) {
						adjacentFields.add(getField(x+1,y));
					}
					if(y < rows-1) {
						adjacentFields.add(getField(x,y+1));
					}
					if(x >= 1 && y >= 1) {
						adjacentFields.add(getField(x-1,y-1));
					}
					if(x < columns-1 && y >= 1) {
						adjacentFields.add(getField(x+1,y-1));
					}
					if(x < columns-1 && y < rows-1) {
						adjacentFields.add(getField(x+1,y+1));
					}
					if(x >= 1 && y < rows-1) {
						adjacentFields.add(getField(x-1,y+1));
					}
				}
			}
		}
		return adjacentFields;
	}
	public void fillBoard() {
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				grid[x][y] = new Field();
			}
		}
	}
	public void plantBombs() {
		for(int i = 0; i < numberOfBombs; i++) {
			int randomX = (int) (Math.random() * columns);
			int randomY = (int) (Math.random() * rows);
			Field field = this.getField(randomX,randomY);
			if(field.isBomb()) {
				i--;
			}else {
				field.setBomb();
			}
		}
	}
	public void checkAdjacentBombs() {
		Arrays.stream(grid)
			.flatMap(Arrays::stream)
			.forEach(field -> 
				field.setAdjacentBombs(
					getAdjacentFields(field).stream()
						.filter(Field::isBomb)
						.mapToInt(f -> 1)
						.sum()
				)
			);
	}
	public List<Field> getEmptyFields(){
		return Arrays.stream(grid)
				.flatMap(Arrays::stream)
				.filter(field -> !field.isBomb())
				.collect(Collectors.toList());
	}
}
