package com.gregswebserver.catan.common.game.gameplay.enums;

/**
 * Created by Greg on 8/10/2014.
 * All of the combinations of dice rolls and their ranks.
 */
public enum DiceRoll {

    Two(2, 1),
    Three(3, 2),
    Four(4, 3),
    Five(5, 4),
    Six(6, 5),
    Seven(7, 0),
    Eight(8, 5),
    Nine(9, 4),
    Ten(10, 3),
    Eleven(11, 2),
    Twelve(12, 1);

    public final int value;
    public final int rank;

    DiceRoll(int value, int rank) {
        this.value = value;
        this.rank = rank;
    }

    public static DiceRoll get(int value) {
        // values() contains an array of enums in order, quickly find the one we need.
        return values()[value - 2];
    }

    public static DiceRoll random() {
        int dieOne = (int) (Math.random() * 6) + 1;
        int dieTwo = (int) (Math.random() * 6) + 1;
        return get(dieOne + dieTwo);
    }
}
