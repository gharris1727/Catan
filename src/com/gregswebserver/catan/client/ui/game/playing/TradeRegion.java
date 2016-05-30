package com.gregswebserver.catan.client.ui.game.playing;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.ui.game.ContextRegion;
import com.gregswebserver.catan.client.ui.game.ResourceCounter;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.game.util.GameResource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends ConfigurableScreenRegion {

    //Required instance information
    private final CatanGame game;
    private final Username local;

    //Optional interaction modules
    private ContextRegion context;

    //TODO: provide an interface to create a new TemporaryTrade.
    //Configuration dependencies
    private RenderMask tradeSize;
    private int panelHeight;
    private int tradeHeight;

    //Sub-regions
    private final TradeListContainer container;
    private final TradeControlPanel panel;

    public TradeRegion(CatanGame game, Username local) {
        super(1, "trade");
        //Save instance details
        this.game = game;
        this.local = local;
        //Create sub-regions
        container = new TradeListContainer(new TradeList());
        panel = new TradeControlPanel();
        //Add everything to the screen.
        add(container);
        add(panel);
    }

    public void setContext(ContextRegion context) {
        this.context = context;
    }

    public void update() {
        container.update();
    }

    @Override
    public void loadConfig(UIConfig config) {
        panelHeight = config.getLayout().getInt("panel.height");
        tradeHeight = config.getLayout().getInt("container.list.element.height");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int scrollHeight = mask.getHeight() - panelHeight;
        RenderMask scrollSize = new RectangularMask(new Dimension(mask.getWidth(), scrollHeight));
        RenderMask panelSize = new RectangularMask(new Dimension(mask.getWidth(), panelHeight));
        container.setMask(scrollSize);
        panel.setMask(panelSize);
        panel.setPosition(new Point(0, scrollHeight));
        tradeSize = new RectangularMask(new Dimension(mask.getWidth(), tradeHeight));
        container.setInsets(new Insets(0,0,0,0));
    }

    public String toString() {
        return "TradeScreenArea";
    }

    private class TradeList extends ScrollingScreenRegion {

        private TradeList() {
            super(1, "list");
            setTransparency(true);
        }

        @Override
        protected void renderContents() {
            clear();
            int height = 0;
            for (Trade t : game.getTrades(local)) {
                TradeListElement elt = new TradeListElement(t);
                elt.setConfig(getConfig());
                elt.setMask(tradeSize);
                add(elt).setPosition(new Point(0, height));
                height += tradeSize.getHeight();
            }
            setMask(new RectangularMask(new Dimension(tradeSize.getWidth(), height)));
        }

        @Override
        public UserEvent onMouseScroll(MouseWheelEvent event) {
            scroll(0, -4 * event.getUnitsToScroll());
            return null;
        }

        @Override
        public UserEvent onMouseDrag(Point p) {
            scroll(0, p.y);
            return null;
        }

        @Override
        public String toString() {
            return "TradeList";
        }

        private class TradeListElement extends ConfigurableScreenRegion {

            private final TiledBackground background;
            private final Trade trade;
            private Point elementOffset;
            private Point elementSpacing;

            private TradeListElement(Trade trade) {
                super(0, "element");
                this.trade = trade;
                background = new EdgedTiledBackground(0, "background");

                add(background).setClickable(this);
            }

            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return TradeList.this.onMouseScroll(event);
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return TradeList.this.onMouseDrag(p);
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                if (context != null)
                    context.targetTrade(trade);
                return null;
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
            }

            @Override
            public void loadConfig(UIConfig config) {
                elementOffset = config.getLayout().getPoint("offset");
                elementSpacing = config.getLayout().getPoint("spacing");
            }

            @Override
            protected void renderContents() {
                assertRenderable();
                clear();
                int index = 0;
                for (GameResource gameResource : GameResource.values()) {
                    ResourceCounter request = new ResourceCounter(2, trade.request, gameResource) {
                        @Override
                        public String toString() {
                            return "InventoryResourceCounter";
                        }
                    };
                    ResourceCounter offer = new ResourceCounter(3, trade.offer, gameResource) {
                        @Override
                        public String toString() {
                            return "InventoryResourceCounter";
                        }
                    };
                    add(request).setClickable(this).setPosition(new Point(
                            elementOffset.x + index * elementSpacing.x,
                            elementOffset.y));
                    request.setConfig(getConfig());
                    add(offer).setClickable(this).setPosition(new Point(
                            elementOffset.x + index * elementSpacing.x,
                            elementOffset.y + elementSpacing.y));
                    offer.setConfig(getConfig());
                    index++;
                }
                add(background);
            }

            @Override
            public String toString() {
                return null;
            }
        }
    }

    private class TradeListContainer extends ScrollingScreenContainer {

        private final TiledBackground background;

        private TradeListContainer(ScrollingScreenRegion scroll) {
            super(0, "container", scroll);
            background = new TiledBackground(0, "background");
            add(background).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            super.resizeContents(mask);
            background.setMask(mask);
        }

        @Override
        public String toString() {
            return "TradeListContainer";
        }
    }

    private class TradeControlPanel extends ConfigurableScreenRegion {

        private final TiledBackground background;

        private TradeControlPanel() {
            super(2, "panel");
            background = new EdgedTiledBackground(0, "background");
            add(background).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        public String toString() {
            return "TradeControlPanel";
        }
    }
}
