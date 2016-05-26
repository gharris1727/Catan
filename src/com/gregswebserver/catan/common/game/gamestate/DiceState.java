package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.util.ReversibleIterator;
import com.gregswebserver.catan.common.util.ReversiblePRNG;

/**
 * Created by greg on 2/21/16.
 * A randomizer to create fair die rolls.
 */
public class DiceState implements ReversibleIterator<DiceRoll> {

    private final ReversiblePRNG prng;

    public DiceState(long seed) {
        this.prng = new ReversiblePRNG(seed);
    }

    @Override
    public boolean hasPrev() {
        return true;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public DiceRoll next() {
        int dieOne = prng.nextInt(6) + 1;
        int dieTwo = prng.nextInt(6) + 1;
        return DiceRoll.get(dieOne + dieTwo);
    }

    @Override
    public DiceRoll prev() {
        int dieTwo = prng.prevInt(6) + 1;
        int dieOne = prng.prevInt(6) + 1;
        return DiceRoll.get(dieOne + dieTwo);
    }

    @Override
    public DiceRoll get() {
        int dieOne = prng.nextInt(6) + 1;
        int dieTwo = prng.nextInt(6) + 1;
        prng.prevInt(6);
        prng.prevInt(6);
        return DiceRoll.get(dieOne + dieTwo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiceState)) return false;

        DiceState that = (DiceState) o;

        return prng.equals(that.prng);
    }

    @Override
    public String toString() {
        return "DiceState(" + prng + ")";
    }
}
