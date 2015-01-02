package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.renderer.StaticGraphic;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.input.clickables.Clickable;
import com.gregswebserver.catan.client.input.clickables.ClickableInventoryItem;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.util.Statics;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 1/1/2015.
 * Class defining the ScreenArea that contains current player information.
 */
public class InventoryArea extends ColorScreenArea {

    //TODO: finish.

    private Player player;

    public InventoryArea(Dimension size, Point position, int priority, Player player) {
        super(position, priority, new InventoryClickable(player));
        this.player = player;
        resize(size);
    }

    protected void render() {
        HashMap<Tradeable, Integer> inventory = player.getInventory();
        ArrayList<Point> positions = new ArrayList<>();
        for (Tradeable t : inventory.keySet()) {
            int num = inventory.get(t);
            Graphic graphic = Statics.nullGraphic;
            if (t instanceof Graphical)
                graphic = ((Graphical) t).getGraphic();
            Point position = new Point();
            positions.add(position);
            Clickable clickable = new ClickableInventoryItem();
            for (int i = 0; i < num; i++) {
                add(new StaticGraphic(
                        graphic,
                        position,
                        0,
                        clickable));
            }
        }
        int divX = (getSize().width - 128) / positions.size();
        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setLocation(divX * (i + 1), 16);
        }
    }

    private static class InventoryClickable implements Clickable {

        public InventoryClickable(Player player) {
        }
    }
}
