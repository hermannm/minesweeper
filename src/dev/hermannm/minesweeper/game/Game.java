package dev.hermannm.minesweeper.game;

import java.util.List;

/** Handles the game logic of Minesweeper. */
public class Game {
    private final Board board;
    private boolean gameOver;
    private boolean gameWon;

    /**
     * Used to ensure that the player's first revealed field is never a bomb,
     * nor adjacent to a bomb.
     */
    private boolean firstReveal;

    /**
     * The bomb counter tracks how many total bombs there are on the board,
     * minus the amount of fields the player has flagged as bombs.
     */
    private int bombCounter;

    /**
     * Instantiates a game with the given board,
     * and sets default values for other fields.
     */
    public Game(Board board) {
        this.board = board;
        bombCounter = board.getNumberOfBombs();
        gameOver = false;
        gameWon = false;
        firstReveal = true;
    }

    /** Instantiates a game with the given board and fields. */
    public Game(Board board, int bombCounter, boolean gameOver, boolean gameWon, boolean firstReveal) {
        this.board = board;
        this.bombCounter = bombCounter;
        this.gameOver = gameOver;
        this.gameWon = gameWon;
        this.firstReveal = firstReveal;
    }

    public Board getBoard() {
        return board;
    }

    public int getBombCounter() {
        return bombCounter;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getGameWon() {
        return gameWon;
    }

    public boolean getFirstReveal() {
        return firstReveal;
    }

    /**
     * Reveals the given field on the board.
     * Recursively reveals adjacent fields that have no adjacent bombs.
     * If field is a bomb, triggers game loss.
     */
    public void revealField(Field field) {
        if (!field.isHidden() || field.flagged() || gameOver || gameWon) {
            return;
        }

        // If this is the player's first click and the field
        // is a bomb or next to a bomb, moves the bombs.
        if (firstReveal) {
            if (field.isBomb() || !(field.getAdjacentBombs() == 0)) {
                board.moveBomb(field);
            }
            firstReveal = false;
        }

        if (field.isBomb()) {
            gameOver();
        } else {
            field.reveal();
            if (field.getAdjacentBombs() == 0) {
                for (Field adjacent : board.getAdjacentFields(field)) {
                    revealField(adjacent);
                }
            }
        }

        checkWin();
    }

    /** Flags/unflags the given field, and adjusts the bomb counter accordingly. */
    public void flagField(Field field) {
        if (!field.isHidden() || gameOver || gameWon) {
            return;
        }

        if (field.flagged()) {
            field.removeFlag();
            bombCounter++;
        } else {
            field.setFlag();
            bombCounter--;
        }
    }

    /**
     * Reveals fields next to the given revealed field,
     * though only if the player has flagged a number of adjacent
     * fields equal to the number of adjacent bombs.
     */
    public void revealAdjacentFields(Field field) {
        if (gameOver || gameWon) {
            return;
        }

        List<Field> adjacentFields = board.getAdjacentFields(field);

        int adjacentFlags = 0;
        for (Field f : adjacentFields) {
            if (f.flagged()) {
                adjacentFlags++;
            }
        }

        if (field.getAdjacentBombs() == adjacentFlags) {
            for (Field f : adjacentFields) {
                this.revealField(f);
            }
        }
    }

    /** Ends the game, revealing all fields. */
    public void gameOver() {
        gameOver = true;

        for (int x = 0; x < board.getColumns(); x++) {
            for (int y = 0; y < board.getRows(); y++) {
                Field field = board.getField(x, y);

                if (field.isBomb()) {
                    field.reveal();
                }
            }
        }

        bombCounter = board.getNumberOfBombs();
    }

    /** Updates the gameWon field if the player has won. */
    public void checkWin() {
        if (gameOver) {
            return;
        }

        // If player has revealed all non-bomb fields, they win.
        for (int x = 0; x < board.getColumns(); x++) {
            for (int y = 0; y < board.getRows(); y++) {
                Field field = board.getField(x, y);

                if (field.isHidden() && !field.isBomb()) {
                    return;
                }
            }
        }

        // Flags remaining unflagged bomb fields.
        for (int x = 0; x < board.getColumns(); x++) {
            for (int y = 0; y < board.getRows(); y++) {
                Field field = board.getField(x, y);

                if (!field.flagged()) {
                    this.flagField(field);
                }
            }
        }

        gameWon = true;
    }
}
