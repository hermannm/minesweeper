package dev.hermannm.minesweeper;

/**
 * Intermediary entry point to fix "runtime components missing".
 * See <a href="https://github.com/javafxports/openjdk-jfx/issues/236#issuecomment-426583174">OpenJDK JavaFX issue</a>.
 */
public class Main {
    public static void main(String[] args) {
        App.main(args);
    }
}
