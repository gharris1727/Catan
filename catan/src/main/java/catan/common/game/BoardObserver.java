package catan.common.game;

import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.paths.Path;
import catan.common.game.board.tiles.Tile;
import catan.common.game.board.towns.Town;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Created by greg on 12/20/16.
 * Class to synchronize accesses to a catan game's board state.
 */
public class BoardObserver {

    private final CatanGame game;

    BoardObserver(CatanGame game) {
        this.game = game;
    }

    public Tile getTile(Coordinate targetCoord) {
        synchronized (game) {
            return game.board.getTile(targetCoord);
        }
    }

    public void eachTile(Consumer<Tile> action) {
        synchronized (game) {
            game.board.eachTile(action);
        }
    }

    public Path getPath(Coordinate targetCoord) {
        synchronized (game) {
            return game.board.getPath(targetCoord);
        }
    }
    public void eachPath(Consumer<Path> action) {
        synchronized (game) {
            game.board.eachPath(action);
        }
    }

    public Town getTown(Coordinate targetCoord) {
        synchronized (game) {
            return game.board.getTown(targetCoord);
        }
    }

    public void eachTown(Consumer<Town> action) {
        synchronized (game) {
            game.board.eachTown(action);
        }
    }

    public Dimension getSize() {
        synchronized (game) {
            return game.board.getSize();
        }
    }

}
