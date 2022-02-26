package dev.hermannm.minesweeper.gui;

import java.util.Map;
import java.util.Map.Entry;

import dev.hermannm.minesweeper.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Scene for the main menu of the game. */
public class MainMenu {
	private Scene scene;
	private Controller controller;

	/**
	 * Configuration of the game mode buttons in the menu.
	 * The integer array is the number of columns, rows and bombs
	 * in the selected game, respectively.
	 */
	private static final Map<String, int[]> GAME_OPTIONS = Map.of(
			"Easy", new int[] { 8, 8, 10 },
			"Normal", new int[] { 16, 16, 40 },
			"Hard", new int[] { 30, 16, 99 },
			"Insanity", new int[] { 30, 16, 470 });

	public MainMenu(Controller controller) {
		this.controller = controller;

		// Sets menu title.
		Label title = new Label(Constants.GAME_NAME);

		// Sets up menu buttons.
		VBox buttons = makeButtons();

		// Adds root container, and attaches title and buttons to it.
		VBox root = new VBox(title, buttons);
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);
		root.setPadding(new Insets(10));

		// Attaches the root container to the scene, and adds the stylesheet.
		this.scene = new Scene(root);
		this.scene.getStylesheets().add((getClass().getResource("/styles.css")).toString());
	}

	/** Returns a container with a button for each game option in this class. */
	private VBox makeButtons() {
		// Adds menu button container.
		VBox buttons = new VBox();
		buttons.getStyleClass().add("container");

		// Goes through the game options configuration, and adds a menu button for each.
		int index = 0;
		for (Entry<String, int[]> gameOption : GAME_OPTIONS.entrySet()) {
			String label = gameOption.getKey();
			Button modeButton = new Button(label + " Mode");

			// Ensures that index never goes out of bounds if length of game options exceeds
			// length of colors.
			int colorIndex = index % Constants.COLORS.length;
			modeButton.getStyleClass().add(Constants.COLORS[colorIndex]);
			index++;

			int[] parameters = gameOption.getValue();
			modeButton.setOnAction(e -> {
				controller.newGame(parameters[0], parameters[1], parameters[2]);
			});

			modeButton.setPrefWidth(250);
			buttons.getChildren().add(modeButton);
		}

		return buttons;
	}

	/** Returns the menu scene. */
	public Scene getScene() {
		return scene;
	}
}
