package com.gregswebserver.catan.game.gameplay.generator;

import java.util.Iterator;

/**
 * Created by Greg on 8/13/2014.
 * A generator that constructs features based on the number of playable tiles
 */
public interface FeatureGenerator<T> {

    public abstract void randomize();

    public abstract Iterator<T> iterator();
}
