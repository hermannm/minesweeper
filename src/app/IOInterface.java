package app;

import java.io.IOException;

public interface IOInterface {
	void save(String filename, Minesweeper game) throws IOException;
	Minesweeper load(String filename) throws IOException;
}
