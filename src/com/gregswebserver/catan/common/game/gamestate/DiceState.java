package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.util.ReversibleIterator;
import com.gregswebserver.catan.common.util.ReversiblePRNG;

/**
 * Created by greg on 2/21/16.
 * A randomizer to create fair die rolls.
 */
public class DiceState implements ReversibleIterator<DiceRoll> {

    private final ReversiblePRNG dieOne;
    private final ReversiblePRNG dieTwo;

    public DiceState(long seed) {
        this.dieOne = new ReversiblePRNG(seed);
        this.dieTwo = new ReversiblePRNG(seed << 32 | seed >>> 32);
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
        DiceRoll next = get();
        dieOne.next();
        dieTwo.next();
        return next;
    }

    @Override
    public DiceRoll prev() {
        dieOne.prev();
        dieTwo.prev();
        return get();
    }

    @Override
    public DiceRoll get() {
        return DiceRoll.get(dieOne.getInt(6) + dieTwo.getInt(6) + 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiceState)) return false;

        DiceState that = (DiceState) o;

        return dieOne.equals(that.dieOne) && dieTwo.equals(that.dieTwo);
    }

    @Override
    public String toString() {
        return "DiceState(" + dieOne + "," + dieTwo + ")";
    }
}
