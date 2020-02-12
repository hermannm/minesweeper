package app;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application{
	GUI gui;
	public Main() {
		gui = new GUI();
	}
	public void start(Stage stage) {
		stage.setTitle("Minesweeper");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("minesweeper.png")));
		gui.setStage(stage);
		stage.setScene(gui.menu());
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
