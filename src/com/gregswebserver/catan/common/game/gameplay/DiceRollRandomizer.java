package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by greg on 2/21/16.
 * A randomizer to create fair die rolls.
 */
public class DiceRollRandomizer implements Iterator<DiceRoll> {

    private final Random random;

    public DiceRollRandomizer(long seed) {
        random = new Random(seed);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public DiceRoll next() {
        int dieOne = random.nextInt(6) + 1;
        int dieTwo = random.nextInt(6) + 1;
        return DiceRoll.get(dieOne + dieTwo);
    }
}
