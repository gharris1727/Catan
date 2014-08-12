package com.gregswebserver.catan.game.gameplay;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Greg on 8/10/2014.
 * Generates a set of DiceRolls based on the number of hexagons needed.
 */
public class DiceGenerator extends AbstractCollection<DiceRoll> {

    private static int[] genOrder = {6, 8, 5, 9, 4, 10, 3, 11, 2, 12};
    private ArrayList<DiceRoll> dice;

    public DiceGenerator(int numTiles) {
        numTiles--; //Always one desert tile.
        dice = new ArrayList<>();
        for (int i = 0; i < numTiles; i++) {
            int value = genOrder[i % genOrder.length];
            dice.add(DiceRoll.get(value));
        }
        Collections.shuffle(dice);
    }

    public Iterator<DiceRoll> iterator() {
        return dice.iterator();
    }

    public int size() {
        return dice.size();
    }
}
