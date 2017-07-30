package catan.common.game.gamestate;

import catan.common.util.ReversibleIterator;
import catan.common.util.ReversiblePRNG;

import java.util.Random;

/**
 * Created by greg on 2/21/16.
 * A randomizer to create fair die rolls.
 */
public class DiceState implements ReversibleIterator<DiceRoll> {

    private final ReversiblePRNG dieOne;
    private final ReversiblePRNG dieTwo;

    public DiceState(long seed) {
        dieOne = new ReversiblePRNG(seed);
        dieTwo = new ReversiblePRNG(new Random(seed).nextLong());
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

        DiceState other = (DiceState) o;

        return dieOne.equals(other.dieOne) && dieTwo.equals(other.dieTwo);
    }

    @Override
    public int hashCode() {
        int result = dieOne.hashCode();
        result = 31 * result + dieTwo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DiceState(" + dieOne + "," + dieTwo + ")";
    }
}
