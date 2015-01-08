package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import static com.gregswebserver.catan.common.resources.cached.GraphicInfo.*;

/**
 * Created by Greg on 8/10/2014.
 * All of the combinations of dice rolls and their ranks.
 */
public enum DiceRoll implements Graphical {

    Two(2, 1, ResourceLoader.getGraphic(DiceTwo)),
    Three(3, 2, ResourceLoader.getGraphic(DiceThree)),
    Four(4, 3, ResourceLoader.getGraphic(DiceFour)),
    Five(5, 4, ResourceLoader.getGraphic(DiceFive)),
    Six(6, 5, ResourceLoader.getGraphic(DiceSix)),
    Seven(7, 0, ResourceLoader.getGraphic(DiceSeven)),
    Eight(8, 5, ResourceLoader.getGraphic(DiceEight)),
    Nine(9, 4, ResourceLoader.getGraphic(DiceNine)),
    Ten(10, 3, ResourceLoader.getGraphic(DiceTen)),
    Eleven(11, 2, ResourceLoader.getGraphic(DiceEleven)),
    Twelve(12, 1, ResourceLoader.getGraphic(DiceTwelve));

    private final int value;
    private final int rank;
    private final Graphic graphic;

    DiceRoll(int value, int rank, Graphic graphic) {
        this.value = value;
        this.rank = rank;
        this.graphic = graphic;
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
