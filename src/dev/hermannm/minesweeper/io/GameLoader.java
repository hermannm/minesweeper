package dev.hermannm.minesweeper.io;

import dev.hermannm.minesweeper.game.Game;

import java.io.IOException;

public interface GameLoader {
    Game load(String filename) throws IOException;
}
