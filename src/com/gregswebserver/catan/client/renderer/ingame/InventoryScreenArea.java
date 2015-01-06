package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.client.graphics.areas.ScreenObject;
import com.gregswebserver.catan.client.graphics.areas.StaticGraphic;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.util.Statics;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryScreenArea extends ColorScreenArea {

    private Player player;

    public InventoryScreenArea(Point position, int priority, Player player) {
        super(position, priority);
        this.player = player;
    }

    protected void render() {
        clear();
        //TODO: clean up this renderer
        HashMap<Tradeable, Integer> inventory = player.getInventory();
        ArrayList<Point> positions = new ArrayList<>();
        for (Tradeable t : inventory.keySet()) {
            int num = inventory.get(t);
            Graphic graphic = Statics.nullGraphic;
            if (t instanceof Graphical)
                graphic = ((Graphical) t).getGraphic();
            Point position = new Point();
            positions.add(position);
            for (int i = 0; i < num; i++) {
                ScreenObject o = new StaticGraphic(position, 0, graphic) {
                    public UserEvent onMouseClick(MouseEvent event) {
                        return new UserEvent(this, UserEventType.Inventory_Clicked, position.x);
                    }
                };
                add(o);
            }
        }
        int divX = (getSize().width - 128) / positions.size();
        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setLocation(divX * (i + 1), 16);
        }
    }
}
