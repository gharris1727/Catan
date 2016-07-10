package catan.common.game.gameplay.generator;

import java.util.Random;

/**
 * Created by Greg on 8/13/2014.
 * A generator that constructs features based on the number of playable tiles
 */
public interface FeatureGenerator<T> extends Iterable<T> {

    void randomize(Random random);
}
