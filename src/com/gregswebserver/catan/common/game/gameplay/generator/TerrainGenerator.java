package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Greg on 8/13/2014.
 * Helper class to generate tiles with balanced terrain types.
 */
public class TerrainGenerator implements FeatureGenerator<Terrain> {

    private ArrayList<Terrain> terrain;

    public TerrainGenerator(int numTiles) {
        terrain = new ArrayList<>(numTiles);
        terrain.add(Terrain.Desert);
        numTiles--;
        for (int i = 0; i < numTiles; i++) {
            terrain.add(Terrain.values()[i % 5]);
        }
    }

    public void randomize() {
        Collections.shuffle(terrain);
    }

    public Iterator<Terrain> iterator() {
        return terrain.iterator();
    }
}
