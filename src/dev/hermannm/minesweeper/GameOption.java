package dev.hermannm.minesweeper;

public record GameOption(String label, int columns, int rows, int numberOfBombs) {
    /**
     * Configuration of the game mode buttons in the main menu.
     */
    public static final GameOption[] OPTIONS = {
        new GameOption("Easy", 8, 8, 10),
        new GameOption("Normal", 16, 16, 40),
        new GameOption("Hard", 30, 16, 99),
        new GameOption("Insanity", 30, 16, 470)
    };
}
