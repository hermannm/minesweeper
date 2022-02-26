package dev.hermannm.minesweeper.gui;

import dev.hermannm.minesweeper.Controller;
import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Board;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI {
	Controller controller;
	private Stage stage;
	private final String[] numberColors = { "blue", "green", "red", "darkblue", "brown", "cyan", "pink", "gray" };

	public GUI(Stage stage) {
		this.controller = new Controller(this);
		this.stage = stage;
		stage.setTitle("Minesweeper");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/minesweeper.png")));
		stage.setScene(this.menu());
		stage.show();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setResizable(false);
	}

	public void updateScene(Scene scene) {
		stage.setScene(scene);
	}

	public Scene boardScene(Board board, boolean gameWon, boolean gameOver, int bombCounter) {
		Button menuButton = new Button(), saveButton = new Button("Save"), loadButton = new Button("Load");
		Label[] bombCounterLabel = { new Label(), new Label(), new Label() };
		HBox bombCounterBox = new HBox(bombCounterLabel[0], bombCounterLabel[1], bombCounterLabel[2]),
				spacing = new HBox();
		BorderPane header = new BorderPane(menuButton, null, spacing, null, bombCounterBox),
				footer = new BorderPane(null, null, loadButton, null, saveButton);
		GridPane grid = new GridPane();
		BorderPane root = new BorderPane(grid, header, null, footer, null);
		BorderPane.setAlignment(grid, Pos.CENTER);
		BorderPane.setMargin(grid, new Insets(10));
		BorderPane.setMargin(header, new Insets(10, 10, 0, 10));
		BorderPane.setMargin(footer, new Insets(0, 10, 10, 10));
		BorderPane.setMargin(menuButton, new Insets(5));
		BorderPane.setMargin(bombCounterBox, new Insets(5, 0, 5, 5));
		BorderPane.setMargin(spacing, new Insets(5, 5, 5, 0));
		BorderPane.setMargin(saveButton, new Insets(5));
		BorderPane.setMargin(loadButton, new Insets(5, 5, 5, 0));
		bombCounterBox.setAlignment(Pos.CENTER_LEFT);
		spacing.setPrefWidth(81);
		menuButton.setPrefSize(45, 45);
		saveButton.setPrefWidth(90);
		loadButton.setPrefWidth(90);
		grid.getStyleClass().addAll("container", "grid");
		header.getStyleClass().add("container");
		footer.getStyleClass().add("container");
		bombCounterBox.getStyleClass().add("bombCounter");
		menuButton.getStyleClass().add("hasBackground");
		if (gameWon) {
			menuButton.getStyleClass().add("won");
		} else if (gameOver) {
			menuButton.getStyleClass().add("lost");
		} else {
			menuButton.getStyleClass().add("ingame");
		}
		menuButton.setOnAction(e -> {
			controller.showMenu();
		});
		String bombCounterStr = (Math.abs(bombCounter) + "");
		for (int i = 0; i < 3; i++) {
			if (i == 0 && bombCounter < 0) {
				bombCounterLabel[i].setText("-");
			} else {
				try {
					bombCounterLabel[i].setText(
							bombCounterStr.substring(bombCounterStr.length() + i - 3, bombCounterStr.length() + i - 2));
				} catch (Exception e) {
					bombCounterLabel[i].setText("0");
				}
			}
			bombCounterLabel[i].setAlignment(Pos.CENTER_RIGHT);
			bombCounterLabel[i].setPrefWidth(27);
			bombCounterLabel[i].getStyleClass().add("red");
		}
		saveButton.setOnAction(e -> {
			controller.save();
		});
		loadButton.setOnAction(e -> {
			controller.load();
		});
		int fieldSize = 30;
		for (int y = 0; y < board.getRows(); y++) {
			grid.getRowConstraints().add(new RowConstraints());
			for (int x = 0; x < board.getColumns(); x++) {
				grid.getColumnConstraints().add(new ColumnConstraints());
				Field field = board.getField(x, y);
				Button fieldButton = new Button();
				fieldButton.setMinSize(fieldSize, fieldSize);
				fieldButton.setMaxSize(fieldSize, fieldSize);
				fieldButton.setOnMousePressed(e -> {
					if (e.getButton() == MouseButton.PRIMARY) {
						menuButton.getStyleClass().add("pressed");
					}
				});
				if (field.isHidden()) {
					if (field.flagged()) {
						fieldButton.getStyleClass().addAll("hasBackground", "flagged");
					}
					fieldButton.setOnMouseReleased(e -> {
						if (e.getButton() == MouseButton.PRIMARY
								&& e.getPickResult().getIntersectedNode() == fieldButton) {
							controller.handleClick(field);
						}
						menuButton.getStyleClass().remove("pressed");
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
								&& (e.getPickResult().getIntersectedNode() == fieldButton || fieldButton
										.getChildrenUnmodifiable().contains(e.getPickResult().getIntersectedNode()))) {
							controller.handleRevealAdjacent(field);
						}
						menuButton.getStyleClass().remove("pressed");
					});
					if (field.isBomb()) {
						fieldButton.setText("*");
					} else if (field.getAdjacentBombs() > 0) {
						fieldButton.setText(field.getAdjacentBombs() + "");
						fieldButton.getStyleClass().add(numberColors[field.getAdjacentBombs() - 1]);
					}
				}
				grid.add(fieldButton, x, y);
			}
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add((getClass().getResource("/styles.css")).toString());
		return scene;
	}

	public Scene menu() {
		Label title = new Label("Minesweeper");
		VBox buttons = new VBox(), root = new VBox(title, buttons);
		String[] modes = { "Easy", "Normal", "Hard", "Insanity" };
		int[][] modeParameters = { { 8, 8, 10 }, { 16, 16, 40 }, { 30, 16, 99 }, { 30, 16, 470 } };
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);
		root.setPadding(new Insets(10));
		buttons.getStyleClass().add("container");
		for (int i = 0; i < modes.length; i++) {
			int j = i;
			String mode = modes[i];
			Button modeButton = new Button(mode + " Mode");
			modeButton.setPrefWidth(250);
			modeButton.getStyleClass().add(numberColors[i]);
			modeButton.setOnAction(e -> {
				controller.newGame(modeParameters[j][0], modeParameters[j][1], modeParameters[j][2]);
			});
			buttons.getChildren().add(modeButton);
		}
		Scene scene = new Scene(root);
		scene.getStylesheets().add((getClass().getResource("/styles.css")).toString());
		return scene;
	}
}
