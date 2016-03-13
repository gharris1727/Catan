package com.gregswebserver.catan.common.game.gameplay.random;

import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.util.ReversibleIterator;
import com.gregswebserver.catan.common.util.ReversiblePRNG;

/**
 * Created by greg on 2/21/16.
 * A randomizer to create fair die rolls.
 */
public class DiceRollRandomizer implements ReversibleIterator<DiceRoll> {

    private final ReversiblePRNG prng;

    public DiceRollRandomizer(long seed) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiceRollRandomizer)) return false;

        DiceRollRandomizer that = (DiceRollRandomizer) o;

        return prng.equals(that.prng);
    }

    @Override
    public String toString() {
        return "DiceRollRandomizer(" + prng + ")";
    }
}
