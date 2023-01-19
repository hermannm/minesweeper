package dev.hermannm.minesweeper.gui;

import dev.hermannm.minesweeper.Controller;
import dev.hermannm.minesweeper.GameOption;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.net.URL;

/** Scene for the main menu of the game. */
public class MainMenu {
    private final Scene scene;
    private final Controller controller;

    public MainMenu(Controller controller) throws FileNotFoundException {
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

        URL stylesheet = getClass().getResource("/styles.css");
        if (stylesheet == null) {
            throw new FileNotFoundException("Stylesheet not found.");
        }
        this.scene.getStylesheets().add(stylesheet.toString());
    }

    /** Returns a container with a button for each game option in this class. */
    private VBox makeButtons() {
        // Adds menu button container.
        VBox buttons = new VBox();
        buttons.getStyleClass().add("container");

        // Goes through the game options configuration, and adds a menu button for each.
        for (int i = 0; i < GameOption.OPTIONS.length; i++) {
            GameOption gameOption = GameOption.OPTIONS[i];
            String label = gameOption.label();
            Button modeButton = new Button(label + " Mode");

            // Ensures that index never goes out of bounds if length of game options exceeds
            // length of colors.
            int colorIndex = i % Constants.COLORS.length;
            modeButton.getStyleClass().add(Constants.COLORS[colorIndex]);

            modeButton.setOnAction(e -> controller.newGame(
                gameOption.columns(),
                gameOption.rows(),
                gameOption.numberOfBombs()
            ));

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
