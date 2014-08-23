package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.Graphical;
import com.gregswebserver.catan.util.Statics;

/**
 * Created by Greg on 8/10/2014.
 * All of the combinations of dice rolls and their ranks.
 */
public enum DiceRoll implements Graphical {

    Two(2, 1, Statics.diceRollTwo),
    Three(3, 2, Statics.diceRollThree),
    Four(4, 3, Statics.diceRollFour),
    Five(5, 4, Statics.diceRollFive),
    Six(6, 5, Statics.diceRollSix),
    Seven(7, 0, Statics.diceRollSeven),
    Eight(8, 5, Statics.diceRollEight),
    Nine(9, 4, Statics.diceRollNine),
    Ten(10, 3, Statics.diceRollTen),
    Eleven(11, 2, Statics.diceRollEleven),
    Twelve(12, 1, Statics.diceRollTwelve);

    private final int value;
    private final int rank;
    private final Graphic graphic;

    DiceRoll(int value, int rank, Graphic graphic) {
        this.value = value;
        this.rank = rank;
        this.graphic = graphic;
    }

    public static DiceRoll get(int value) {
        // values() contains an hexArray of enums in order, quickly find the one we need.
        return values()[value - 2];
    }

    public static DiceRoll random() {
        int dieOne = (int) (Math.random() * 6) + 1;
        int dieTwo = (int) (Math.random() * 6) + 1;
        return get(dieOne + dieTwo);
    }

    public int getValue() {
        return value;
    }

    public int getRank() {
        return rank;
    }

    public Graphic getGraphic() {
        return graphic;
    }
}
