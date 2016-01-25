package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.masks.RoundedMask;
import com.gregswebserver.catan.common.resources.GraphicSet;

import java.awt.*;

/**
 * Created by Greg on 8/10/2014.
 * All of the combinations of dice rolls and their ranks.
 */
public enum DiceRoll implements Graphical {

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

    private static final GraphicSet graphics;

    static {
        graphics = new GraphicSet("catan.graphics.game.dice", RoundedMask.class);
    }

    private final int value;
    private final int rank;

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

    public int getValue() {
        return value;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public Graphic getGraphic() {
        return graphics.getGraphic(ordinal());
    }

    public Point getOffset() {
        return Client.staticConfig.getPoint("catan.graphics.game.dice.offset");
    }
}
