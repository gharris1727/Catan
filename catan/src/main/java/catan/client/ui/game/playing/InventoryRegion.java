package catan.client.ui.game.playing;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.*;
import catan.client.ui.game.CardList;
import catan.common.game.players.Player;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 * Area responsible for rendering the inventory of the player.
 */
public class InventoryRegion extends ConfigurableScreenRegion implements Updatable {

    //Configuration Dependencies
    private int usernameHeight;
    private Point cardBorder;

    //Sub-regions
    private final TiledBackground background;
    private final TextLabel username;
    private final CardList cards;

    public InventoryRegion(Player player) {
        super(player.getName() + "\'sInventory", 2, "inventory");
        //Create sub-regions
        background = new EdgedTiledBackground();
        username = new TextLabel("Username", 1, "username", player.getName().username + " " + player.getTeamColor());
        cards = new CardList("CardList", 2, "cards", player.getInventory(), player.getDevelopmentCards());
        //Add everything to the screen.
        add(background).setClickable(this);
        add(username).setClickable(this);
        add(cards).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void loadConfig(UIConfig config) {
        usernameHeight = config.getLayout().getInt("username.height");
        cardBorder = config.getLayout().getPoint("border");
    }

    @Override
    protected void renderContents() {
        cards.setPosition(new Point(cardBorder.x, cardBorder.y + usernameHeight));
        int width = getMask().getWidth() - 2 * cardBorder.x;
        int height = getMask().getHeight() - 2 * cardBorder.y;
        cards.setMask(new RectangularMask(new Dimension(width, height)));
        center(username).y = usernameHeight;
    }

    @Override
    public void update() {
        cards.forceRender();
    }
}
