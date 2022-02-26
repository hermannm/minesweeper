package dev.hermannm.minesweeper.io;

import java.io.IOException;

import dev.hermannm.minesweeper.game.Game;

public interface IOInterface {
	void save(String filename, Game game) throws IOException;

	Game load(String filename) throws IOException;
}
