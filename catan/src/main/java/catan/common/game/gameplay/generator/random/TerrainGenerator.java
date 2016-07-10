package catan.common.game.gameplay.generator.random;

import catan.common.game.board.Terrain;
import catan.common.game.gameplay.generator.FeatureGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Greg on 8/13/2014.
 * Helper class to generate tiles with balanced terrain types.
 */
public class TerrainGenerator implements FeatureGenerator<Terrain> {

    private final ArrayList<Terrain> terrain;

    public TerrainGenerator(int numTiles) {
        terrain = new ArrayList<>(numTiles);
        terrain.add(Terrain.Desert);
        numTiles--;
        for (int i = 0; i < numTiles; i++) {
            terrain.add(Terrain.values()[i % 5]);
        }
    }

    @Override
    public void randomize(Random random) {
        Collections.shuffle(terrain, random);
    }

    @Override
    public Iterator<Terrain> iterator() {
        return terrain.iterator();
    }
}
