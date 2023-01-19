package dev.hermannm.minesweeper.io;

import dev.hermannm.minesweeper.game.Board;
import dev.hermannm.minesweeper.game.Field;
import dev.hermannm.minesweeper.game.Game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/** Implements saving and loading games to and from text files. */
public class SaveFileHandler implements GameSaver, GameLoader {
    /** Saves the given game to a file of the given name. */
    public void save(Game game, String filename) throws IOException {
        PrintWriter writer = new PrintWriter(filename);

        String gameStatusLine = String.format(
            "%d,%b,%b,%b",
            game.getBombCounter(),
            game.getGameOver(),
            game.getGameWon(),
            game.getFirstReveal()
        );
        writer.println(gameStatusLine);

        Board board = game.getBoard();
        String boardConfigLine = String.format(
            "%d,%d,%d",
            board.getColumns(),
            board.getRows(),
            board.getNumberOfBombs()
        );
        writer.println(boardConfigLine);

        for (int x = 0; x < board.getColumns(); x++) {
            for (int y = 0; y < board.getRows(); y++) {
                Field field = board.getField(x, y);
                String fieldLine = String.format(
                    "%d,%b,%b,%b",
                    field.getAdjacentBombs(),
                    field.isBomb(),
                    field.isHidden(),
                    field.flagged()
                );
                writer.println(fieldLine);
            }
        }

        writer.flush();
        writer.close();
    }

    public Game load(String filename) throws IOException {
        Scanner scanner = new Scanner(new File(filename));

        // Parses the first line with the game status.
        String[] gameLine = scanner.nextLine().split(",");
        int bombCounter = Integer.parseInt(gameLine[0]);
        boolean gameOver = Boolean.parseBoolean(gameLine[1]);
        boolean gameWon = Boolean.parseBoolean(gameLine[2]);
        boolean firstReveal = Boolean.parseBoolean(gameLine[3]);

        // Parses the second line with the board config.
        String[] boardLine = scanner.nextLine().split(",");
        int columns = Integer.parseInt(boardLine[0]);
        int rows = Integer.parseInt(boardLine[1]);
        int numberOfBombs = Integer.parseInt(boardLine[2]);

        // Parses the remaining lines with the status for each field.
        Field[][] grid = new Field[columns][rows];
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                String[] fieldLine = scanner.nextLine().split(",");

                int adjacentBombs = Integer.parseInt(fieldLine[0]);
                boolean bomb = Boolean.parseBoolean(fieldLine[1]);
                boolean hidden = Boolean.parseBoolean(fieldLine[2]);
                boolean flag = Boolean.parseBoolean(fieldLine[3]);

                grid[x][y] = new Field(adjacentBombs, bomb, hidden, flag);
            }
        }

        scanner.close();

        // Constructs a new board and game from the parsed results.
        Board board = new Board(grid, columns, rows, numberOfBombs);
        return new Game(board, bombCounter, gameOver, gameWon, firstReveal);
    }
}
