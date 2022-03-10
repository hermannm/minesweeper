package dev.hermannm.minesweeper.io;

import java.io.IOException;

import dev.hermannm.minesweeper.game.Game;

public interface GameSaver {
    void save(Game game, String filename) throws IOException;
}
