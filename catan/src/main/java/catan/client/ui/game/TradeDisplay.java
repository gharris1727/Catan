package catan.client.ui.game;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.ConfigurableScreenRegion;
import catan.client.graphics.ui.EdgedTiledBackground;
import catan.client.graphics.ui.TiledBackground;
import catan.client.graphics.ui.UIConfig;
import catan.client.structure.GameManager;
import catan.common.game.gameplay.trade.Trade;

import java.awt.*;

/**
 * Created by greg on 6/4/16.
 * A screen object to display a Trade object.
 */
public abstract class TradeDisplay extends ConfigurableScreenRegion {

    //Config dependencies
    private Point elementBorder;

    //Sub-regions
    private final TiledBackground background;
    private final CardList request;
    private final CardList offer;

    protected TradeDisplay(String name, int priority, String configKey, GameManager manager, Trade trade) {
        super(name, priority, configKey);
        background = new EdgedTiledBackground();
        request = new CardList("RequestCardList", 1, "request", manager, trade.getRequest(), null);
        offer = new CardList("OfferCardList", 1, "offer", manager, trade.getOffer(), null);
        add(background).setClickable(this);
        add(request).setClickable(this);
        add(offer).setClickable(this);
    }

    @Override
    public void loadConfig(UIConfig config) {
        elementBorder = config.getLayout().getPoint("border");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        int width = (getMask().getWidth() - (elementBorder.x * 4)) / 2;
        int height = getMask().getHeight() - (elementBorder.y * 2);
        RenderMask counterSize = new RectangularMask(new Dimension(width, height));
        request.setMask(counterSize);
        offer.setMask(counterSize);
        request.setPosition(elementBorder);
        offer.setPosition(new Point((3 * elementBorder.x) + width, elementBorder.y));
    }
}
