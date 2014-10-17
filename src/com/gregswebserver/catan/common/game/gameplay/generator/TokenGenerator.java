package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.gameplay.DiceRoll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Greg on 8/10/2014.
 * Helper class to generate the correct number of tokens based on the number of tiles.
 */
public class TokenGenerator implements FeatureGenerator<DiceRoll> {

    private final static int[] genOrder = {6, 8, 5, 9, 4, 10, 3, 11, 2, 12};
    private ArrayList<DiceRoll> dice;

    public TokenGenerator(int numTokens) {
        dice = new ArrayList<>(numTokens);
        for (int i = 0; i < numTokens; i++) {
            int value = genOrder[i % genOrder.length];
            dice.add(DiceRoll.get(value));
        }
    }

    public void randomize() {
        Collections.shuffle(dice);
    }

    public Iterator<DiceRoll> iterator() {
        return dice.iterator();
    }
}
