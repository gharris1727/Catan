package com.gregswebserver.catan.client.ui.game;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.players.Player;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends ConfigurableScreenRegion {

    private final Player player;

    private final TiledBackground background;
    private final TextLabel username;
    private Point elementOffset;
    private Point elementSpacing;
    private int usernameHeight;

    public InventoryRegion(CatanGame game, Username user) {
        super(2, "inventory");
        //Store instance information.
        this.player = game.getPlayers().getPlayer(user);
        //Create sub-regions
        background = new EdgedTiledBackground(0, "background");
        username = new TextLabel(1, "username", user.username);
        //Add everything to the screen.
        add(background).setClickable(this);
        add(username).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void loadConfig(UIConfig config) {
        elementOffset = config.getLayout().getPoint("element.offset");
        elementSpacing = config.getLayout().getPoint("element.spacing");
        usernameHeight = config.getLayout().getInt("username.position.y");
    }   

    @Override
    protected void renderContents() {
        assertRenderable();
        clear();
        int index = 0;
        for (GameResource gameResource : GameResource.values()) {
            ResourceCounter element = new ResourceCounter(2, player.getInventory(), gameResource) {
                @Override
                public String toString() {
                    return "InventoryResourceCounter";
                }

                @Override
                protected void renderContents() {
                    int current = player.getInventory().get(gameResource);
                    count.setText("" + current);
                    super.renderContents();
                }
            };
            add(element).setClickable(this).setPosition(new Point(
                    elementOffset.x + index * elementSpacing.x,
                    elementOffset.y));
            element.setConfig(getConfig());
            index++;
        }
        //Add everything to the screen.
        add(background).setClickable(this);
        add(username).setClickable(this);
        center(username).y = usernameHeight;
    }

    public String toString() {
        return "InventoryScreenArea " + player;
    }
}
