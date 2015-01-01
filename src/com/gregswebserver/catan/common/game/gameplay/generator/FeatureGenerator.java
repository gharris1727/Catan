package com.gregswebserver.catan.common.game.gameplay.generator;

/**
 * Created by Greg on 8/13/2014.
 * A generator that constructs features based on the number of playable tiles
 */
public interface FeatureGenerator<T> extends Iterable<T> {

    public abstract void randomize();
}
