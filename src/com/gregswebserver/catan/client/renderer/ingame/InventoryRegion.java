package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;
import com.gregswebserver.catan.common.game.player.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends ScreenRegion {

    private Player player;

    public InventoryRegion(Point position, int priority, RenderMask mask, Player player) {
        super(position, priority, mask);
        this.player = player;
    }

    protected void renderContents() {
        clear();
        //TODO: rewrite.
        HashMap<Tradeable, Integer> inventory = player.getInventory();
        List<Point> positions = new ArrayList<>();
        for (Map.Entry<Tradeable, Integer> e : inventory.entrySet()) {
            Tradeable t = e.getKey();
            int n = e.getValue();
            if (t instanceof Graphical) {
                Graphic g = ((Graphical) t).getGraphic();
                Point position = new Point();
                positions.add(position);
                for (int i = 0; i < n; i++) {
                    ScreenObject o = new StaticObject(position, 0, g) {
                        public UserEvent onMouseClick(MouseEvent event) {
                            return new UserEvent(this, UserEventType.Inventory_Clicked, position.x);
                        }

                        public String toString() {
                            return "Inventory Item #" + position.x;
                        }
                    };
                    add(o);
                }
            }
        }
        int divX = (getMask().getWidth() - 128) / positions.size();
        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setLocation(divX * (i + 1), 16);
        }
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
