package dev.hermannm.minesweeper.gui;

import dev.hermannm.minesweeper.Controller;
import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Board;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

public class PlayMode {
	private Scene scene;
	private Controller controller;
	private Board board;

	private Label[] bombCounterLabel;
	private Button[][] boardGrid;
	private Button menuButton;

	public PlayMode(Controller controller, Board board, boolean gameWon, boolean gameOver, int bombCounter) {
		this.controller = controller;
		this.board = board;
		this.boardGrid = new Button[board.getColumns()][board.getRows()];
		this.bombCounterLabel = new Label[3];

		makeMenuButton();
		updateMenuButton(gameWon, gameOver);

		Button saveButton = makeSaveButton();
		Button loadButton = makeLoadButton();
		HBox bombCounterBox = makeBombCounter(bombCounter);
		BorderPane header = makeHeader(menuButton, bombCounterBox);
		BorderPane footer = makeFooter(saveButton, loadButton);
		GridPane grid = makeGrid(menuButton);
		BorderPane root = new BorderPane(grid, header, null, footer, null);

		this.scene = new Scene(root);
		this.scene.getStylesheets().add((getClass().getResource("/styles.css")).toString());
	}

	/**
	 * Returns a button that leads back to the main menu.
	 * Changes appearance based on the given gameWon and gameOver parameters.
	 */
	private Button makeMenuButton() {
		this.menuButton = new Button();

		BorderPane.setMargin(menuButton, new Insets(5));
		menuButton.setPrefSize(45, 45);

		menuButton.setOnAction(e -> {
			controller.showMenu();
		});

		menuButton.getStyleClass().add("hasBackground");

		return menuButton;
	}

	/** Sets button icon depending on the game state. */
	public void updateMenuButton(boolean gameWon, boolean gameOver) {
		// Clears old style classes.
		menuButton.getStyleClass().removeAll("won", "lost", "ingame");

		if (gameWon) {
			menuButton.getStyleClass().add("won");
		} else if (gameOver) {
			menuButton.getStyleClass().add("lost");
		} else {
			menuButton.getStyleClass().add("ingame");
		}
	};

	/** Returns a button that saves the game to a file. */
	private Button makeSaveButton() {
		Button saveButton = new Button("Save");

		BorderPane.setMargin(saveButton, new Insets(5));
		saveButton.setPrefWidth(90);

		saveButton.setOnAction(e -> {
			controller.save();
		});

		return saveButton;
	}

	/** Returns a button that loads a game from a file. */
	private Button makeLoadButton() {
		Button loadButton = new Button("Load");

		BorderPane.setMargin(loadButton, new Insets(5, 5, 5, 0));
		loadButton.setPrefWidth(90);

		loadButton.setOnAction(e -> {
			controller.load();
		});

		return loadButton;
	}

	/**
	 * Returns a container with a label for each digit in the given bomb counter.
	 * Assumes that bombCounter has at most 3 digits if it's positive,
	 * or 2 digits if it's negative.
	 */
	private HBox makeBombCounter(int bombCounter) {
		bombCounterLabel = new Label[] { new Label(), new Label(), new Label() };

		// Goes through each digit from the right and sets labels accordingly.
		for (int i = 0; i < 3; i++) {
			bombCounterLabel[i].setAlignment(Pos.CENTER_RIGHT);
			bombCounterLabel[i].setPrefWidth(27);
			bombCounterLabel[i].getStyleClass().add("red");
		}

		// Sets up the container for the bomb counter labels.
		HBox bombCounterBox = new HBox(bombCounterLabel[0], bombCounterLabel[1], bombCounterLabel[2]);
		BorderPane.setMargin(bombCounterBox, new Insets(5, 0, 5, 5));
		bombCounterBox.setAlignment(Pos.CENTER_LEFT);
		bombCounterBox.getStyleClass().add("bombCounter");

		return bombCounterBox;
	}

	public void updateBombCounter(int bombCounter) {
		String bombCounterStr = String.valueOf(Math.abs(bombCounter));

		for (int i = 0; i < 3; i++) {
			if (i == 2 && bombCounter < 0) {
				bombCounterLabel[i].setText("-");
			} else {
				try {
					int digitIndex = bombCounterStr.length() - i - 1;
					char digit = bombCounterStr.charAt(digitIndex);
					bombCounterLabel[i].setText(String.valueOf(digit));
				} catch (Exception e) {
					bombCounterLabel[i].setText("0");
				}
			}
		}
	}

	/** Returns a container with the given buttons. */
	private BorderPane makeHeader(Node menuButton, Node bombCounter) {
		// Sets up spacing between elements of the header.
		HBox spacing = new HBox();
		BorderPane.setMargin(spacing, new Insets(5, 5, 5, 0));
		spacing.setPrefWidth(81);

		// Attaches buttons to header.
		BorderPane header = new BorderPane(menuButton, null, spacing, null, bombCounter);
		BorderPane.setMargin(header, new Insets(10, 10, 0, 10));
		header.getStyleClass().add("container");

		return header;
	}

	/** Returns a container with the given buttons. */
	private BorderPane makeFooter(Node saveButton, Node loadButton) {
		// Attaches buttons to footer.
		BorderPane footer = new BorderPane(null, null, loadButton, null, saveButton);
		BorderPane.setMargin(footer, new Insets(0, 10, 10, 10));
		footer.getStyleClass().add("container");

		return footer;
	}

	/** Returns a grid to hold the game's fields. */
	private GridPane makeGrid(Button menuButton) {
		GridPane grid = new GridPane();
		BorderPane.setAlignment(grid, Pos.CENTER);
		BorderPane.setMargin(grid, new Insets(10));
		grid.getStyleClass().addAll("container", "grid");

		int fieldSize = 30;

		for (int y = 0; y < board.getRows(); y++) {
			grid.getRowConstraints().add(new RowConstraints());

			for (int x = 0; x < board.getColumns(); x++) {
				grid.getColumnConstraints().add(new ColumnConstraints());
				Button fieldButton = makeFieldButton(fieldSize, menuButton);
				boardGrid[x][y] = fieldButton;
				grid.add(fieldButton, x, y);
			}
		}

		updateGrid();

		return grid;
	}

	public void updateGrid() {
		for (int y = 0; y < board.getRows(); y++) {
			for (int x = 0; x < board.getColumns(); x++) {
				Field field = board.getField(x, y);
				Button fieldButton = boardGrid[x][y];
				updateFieldButton(fieldButton, field);
			}
		}
	}

	private Button makeFieldButton(int fieldSize, Button menuButton) {
		Button fieldButton = new Button();

		fieldButton.setMinSize(fieldSize, fieldSize);
		fieldButton.setMaxSize(fieldSize, fieldSize);

		fieldButton.setOnMousePressed(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				menuButton.getStyleClass().add("pressed");
			}
		});

		fieldButton.setOnMouseReleased(e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				menuButton.getStyleClass().remove("pressed");
			}
		});

		return fieldButton;
	}

	public void updateFieldButton(Button fieldButton, Field field) {
		fieldButton.getStyleClass().clear();

		if (field.isHidden()) {
			if (field.flagged()) {
				fieldButton.getStyleClass().addAll("hasBackground", "flagged");
			}

			fieldButton.setOnMouseReleased(e -> {
				if (e.getButton() == MouseButton.PRIMARY && e.getPickResult().getIntersectedNode() == fieldButton) {
					controller.handleClick(field);
				}

			});

			fieldButton.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.SECONDARY) {
					controller.handleFlag(field);
				}
			});
		} else {
			fieldButton.getStyleClass().add("revealed");

			fieldButton.setOnMouseReleased(e -> {
				if (e.getButton() == MouseButton.PRIMARY
						&& (e.getPickResult().getIntersectedNode() == fieldButton
								|| fieldButton.getChildrenUnmodifiable()
										.contains(e.getPickResult().getIntersectedNode()))) {
					controller.handleRevealAdjacent(field);
				}
			});

			if (field.isBomb()) {
				fieldButton.setText("*");
			} else if (field.getAdjacentBombs() > 0) {
				fieldButton.setText(field.getAdjacentBombs() + "");
				fieldButton.getStyleClass().add(Constants.COLORS[field.getAdjacentBombs() - 1]);
			}
		}
	}

	/** Returns the playmode scene. */
	public Scene getScene() {
		return scene;
	}
}
