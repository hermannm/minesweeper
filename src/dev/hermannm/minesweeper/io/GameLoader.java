package dev.hermannm.minesweeper.io;

import java.io.IOException;

import dev.hermannm.minesweeper.game.Game;

public interface GameLoader {
    Game load(String filename) throws IOException;
}
