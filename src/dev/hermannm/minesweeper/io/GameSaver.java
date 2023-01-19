package dev.hermannm.minesweeper.io;

import dev.hermannm.minesweeper.game.Game;

import java.io.IOException;

public interface GameSaver {
    void save(Game game, String filename) throws IOException;
}
