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

import java.io.FileNotFoundException;
import java.net.URL;

/** Scene for the active playmode of the game. */
public class PlayMode {
    private final Scene scene;
    private final Controller controller;

    // UI elements stored as state to avoid full rerender on update.
    private Label[] bombCounterLabel;
    private Button menuButton;
    private Button[][] boardGrid;

    public PlayMode(
        Controller controller,
        Board board,
        boolean gameWon,
        boolean gameOver,
        int bombCounter
    ) throws FileNotFoundException {
        this.controller = controller;

        // Sets up the bomb counter, and updates it with starting value.
        HBox bombCounterBox = makeBombCounter();
        updateBombCounter(bombCounter);

        // Sets up the menu button, and updates it with starting values.
        Button menuButton = makeMenuButton();
        updateMenuButton(gameWon, gameOver);

        // Sets up the board grid, and updates it based on the board.
        GridPane grid = makeGrid(board);
        updateGrid(board);

        // Adds menu button and bomb counter and header.
        BorderPane header = makeHeader(menuButton, bombCounterBox);

        // Sets up footer with save and load buttons.
        Button saveButton = makeSaveButton();
        Button loadButton = makeLoadButton();
        BorderPane footer = makeFooter(saveButton, loadButton);

        BorderPane root = new BorderPane(grid, header, null, footer, null);

        this.scene = new Scene(root);

        URL stylesheet = getClass().getResource("/styles.css");
        if (stylesheet == null) {
            throw new FileNotFoundException("Stylesheet not found.");
        }
        this.scene.getStylesheets().add(stylesheet.toString());
    }

    /** Returns the playmode scene. */
    public Scene getScene() {
        return scene;
    }

    /**
     * Returns a container with a label for each digit in the given bomb counter,
     * and initializes the bombCounterLabel state.
     */
    private HBox makeBombCounter() {
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

    /**
     * Updates the bomb counter labels with the given value.
     * Assumes that bombCounter has at most 3 digits if it's positive,
     * or 2 digits if it's negative.
     */
    public void updateBombCounter(int bombCounter) {
        String bombCounterStr = String.valueOf(Math.abs(bombCounter));

        for (int i = 0; i < 3; i++) {
            if (i == 0 && bombCounter < 0) {
                bombCounterLabel[i].setText("-");
            } else {
                try {
                    int digitIndex = bombCounterStr.length() + i - 3;
                    char digit = bombCounterStr.charAt(digitIndex);
                    bombCounterLabel[i].setText(String.valueOf(digit));
                } catch (Exception e) {
                    bombCounterLabel[i].setText("0");
                }
            }
        }
    }

    /**
     * Creates a button that leads back to the main menu,
     * stores it on this object, and returns it.
     */
    private Button makeMenuButton() {
        this.menuButton = new Button();

        BorderPane.setMargin(menuButton, new Insets(5));
        menuButton.setPrefSize(45, 45);

        menuButton.setOnAction(e -> controller.showMenu());

        menuButton.getStyleClass().add("hasBackground");

        return menuButton;
    }

    /** Sets menu button icon depending on the game state. */
    public void updateMenuButton(boolean gameWon, boolean gameOver) {
        // Clears old style classes.
        menuButton.getStyleClass().removeAll("won", "lost", "ingame", "pressed");

        if (gameWon) {
            menuButton.getStyleClass().add("won");
        } else if (gameOver) {
            menuButton.getStyleClass().add("lost");
        } else {
            menuButton.getStyleClass().add("ingame");
        }
    }

    /**
     * Returns a grid with buttons for each of the board's fields,
     * and initializes the boardGrid state.
     */
    private GridPane makeGrid(Board board) {
        GridPane grid = new GridPane();
        this.boardGrid = new Button[board.getColumns()][board.getRows()];

        BorderPane.setAlignment(grid, Pos.CENTER);
        BorderPane.setMargin(grid, new Insets(10));
        grid.getStyleClass().addAll("container", "grid");

        int fieldSize = 30;

        // Goes through each row and column of the board,
        // and sets up a button for each field.
        for (int x = 0; x < board.getColumns(); x++) {
            grid.getColumnConstraints().add(new ColumnConstraints());

            for (int y = 0; y < board.getRows(); y++) {
                grid.getRowConstraints().add(new RowConstraints());

                Button fieldButton = makeFieldButton(fieldSize);
                boardGrid[x][y] = fieldButton;
                grid.add(fieldButton, x, y);
            }
        }

        return grid;
    }

    /** Returns a button for a board field, of the given field size. */
    private Button makeFieldButton(int fieldSize) {
        Button fieldButton = new Button();

        fieldButton.setMinSize(fieldSize, fieldSize);
        fieldButton.setMaxSize(fieldSize, fieldSize);

        // Sets the field button to change the menu button icon while pressed down.
        fieldButton.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                menuButton.getStyleClass().add("pressed");
            }
        });
        fieldButton.setOnMouseReleased(e -> {
            menuButton.getStyleClass().remove("pressed");
            menuButton.getStyleClass().add("ingame");
        });

        return fieldButton;
    }

    /** Updates each button in the grid to reflect the fields on the board. */
    public void updateGrid(Board board) {
        for (int x = 0; x < board.getColumns(); x++) {
            for (int y = 0; y < board.getRows(); y++) {
                Field field = board.getField(x, y);
                Button fieldButton = boardGrid[x][y];
                updateFieldButton(fieldButton, field);
            }
        }
    }

    /** Updates the given field button to reflec the given field. */
    public void updateFieldButton(Button fieldButton, Field field) {
        // Clears out outdated styles.
        fieldButton.getStyleClass().removeAll("hasBackground", "flagged", "revealed");

        if (field.isHidden()) {
            fieldButton.setText("");

            // Sets flag icon.
            if (field.flagged()) {
                fieldButton.getStyleClass().addAll("hasBackground", "flagged");
            }

            // Left click to reveal field.
            fieldButton.setOnMouseReleased(e -> {
                if (e.getButton() == MouseButton.PRIMARY && e.getPickResult().getIntersectedNode() == fieldButton) {
                    controller.handleClick(field);
                }
            });

            // Right click to flag field.
            fieldButton.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    controller.handleFlag(field);
                }
            });
        } else {
            fieldButton.getStyleClass().add("revealed");

            // Left click revealed field to reveal adjacent ones.
            fieldButton.setOnMouseReleased(e -> {
                if (e.getButton() == MouseButton.PRIMARY
                        && (e.getPickResult().getIntersectedNode() == fieldButton
                                || fieldButton.getChildrenUnmodifiable()
                                        .contains(e.getPickResult().getIntersectedNode()))) {
                    controller.handleRevealAdjacent(field);
                }
            });

            // Set revealed field text.
            if (field.isBomb()) {
                fieldButton.setText("*");
            } else if (field.getAdjacentBombs() > 0) {
                fieldButton.setText(String.valueOf(field.getAdjacentBombs()));
                fieldButton.getStyleClass().add(Constants.COLORS[field.getAdjacentBombs() - 1]);
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

    /** Returns a button that saves the game to a file. */
    private Button makeSaveButton() {
        Button saveButton = new Button("Save");

        BorderPane.setMargin(saveButton, new Insets(5));
        saveButton.setPrefWidth(90);

        saveButton.setOnAction(e -> controller.save());

        return saveButton;
    }

    /** Returns a button that loads a game from a file. */
    private Button makeLoadButton() {
        Button loadButton = new Button("Load");

        BorderPane.setMargin(loadButton, new Insets(5, 5, 5, 0));
        loadButton.setPrefWidth(90);

        loadButton.setOnAction(e -> controller.load());

        return loadButton;
    }

    /** Returns a container with the given buttons. */
    private BorderPane makeFooter(Node saveButton, Node loadButton) {
        // Attaches buttons to footer.
        BorderPane footer = new BorderPane(null, null, loadButton, null, saveButton);
        BorderPane.setMargin(footer, new Insets(0, 10, 10, 10));
        footer.getStyleClass().add("container");

        return footer;
    }
}
