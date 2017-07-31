package catan.common.game.gameplay.generator;

import catan.common.game.board.Terrain;

import java.util.*;

/**
 * Created by Greg on 8/13/2014.
 * Helper class to generate tiles with balanced terrain types.
 */
public class RandomTerrainGenerator implements FeatureGenerator<Terrain> {

    private final List<Terrain> terrain;

    public RandomTerrainGenerator(int numTiles) {
        terrain = new ArrayList<>(numTiles);
        terrain.add(Terrain.Desert);
        for (int i = 0; i < numTiles - 1; i++) {
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
