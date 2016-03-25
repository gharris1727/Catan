package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.ui.UIScreen;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.gameplay.enums.GameResource;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends UIScreen {

    //TODO: provide an interface to create a new TemporaryTrade.

    private final Point elementOffset;
    private final Point elementSpacing;
    private RenderMask tradeSize;

    private final Username local;
    
    private final TiledBackground background;
    private final TradeList list;
    private final TradeControlPanel panel;

    public TradeRegion(UIScreen parent, CatanGame game, Username local) {
        super(1, parent, "trade");
        //Load layout details.
        elementOffset = getLayout().getPoint("element.offset");
        elementSpacing = getLayout().getPoint("element.spacing");
        //Save instance details
        this.local = local;
        //Create sub-regions
        background = new TiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
        list = new TradeList(game);
        panel = new TradeControlPanel();
        //Add everything to the screen.
        add(background).setClickable(this);
        add(list);
        add(panel);
    }

    public void update() {
        list.forceRender();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int panelHeight = getLayout().getInt("panel.height");
        int scrollHeight = mask.getHeight() - panelHeight;
        RenderMask scrollSize = new RectangularMask(new Dimension(mask.getWidth(), scrollHeight));
        RenderMask panelSize = new RectangularMask(new Dimension(mask.getWidth(), panelHeight));
        background.setMask(scrollSize);
        list.setHostView(scrollSize, new Insets(0,0,0,0));
        panel.setMask(panelSize);
        panel.setPosition(new Point(0, scrollHeight));
        tradeSize = new RectangularMask(new Dimension(mask.getWidth(), getLayout().getInt("element.height")));
    }

    public String toString() {
        return "TradeScreenArea";
    }

    private class TradeList extends ScrollingScreenRegion {

        private final CatanGame game;

        private TradeList(CatanGame game) {
            super(0);
            this.game = game;
            setTransparency(true);
        }

        @Override
        protected void renderContents() {
            clear();
            int height = 1;
            for (Trade t : game.getTrades(TradeRegion.this.local)) {
                TradeListElement elt = new TradeListElement(t);
                elt.setStyle(getStyle());
                elt.setMask(tradeSize);
                add(elt).setPosition(new Point(0, height));
                height += tradeSize.getHeight();
            }
            if (height > 1)
                height--;
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

        private class TradeListElement extends StyledScreenRegion {

            private final TiledBackground background;
            private final Trade trade;

            protected TradeListElement(Trade trade) {
                super(0);
                this.trade = trade;
                background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
                int index = 0;
                for (GameResource gameResource : GameResource.values()) {
                    ResourceCounter request = new ResourceCounter(2, TradeRegion.this, trade.request, gameResource) {
                        @Override
                        public String toString() {
                            return "InventoryResourceCounter";
                        }
                    };
                    ResourceCounter offer = new ResourceCounter(3, TradeRegion.this, trade.offer, gameResource) {
                        @Override
                        public String toString() {
                            return "InventoryResourceCounter";
                        }
                    };
                    add(request).setClickable(this).setPosition(new Point(
                            elementOffset.x + index * elementSpacing.x,
                            elementOffset.y));
                    add(offer).setClickable(this).setPosition(new Point(
                            elementOffset.x + index * elementSpacing.x,
                            elementOffset.y + elementSpacing.y));
                    index++;
                }
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
                return new UserEvent(this, UserEventType.Trade_Clicked, trade);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
            }

            @Override
            public String toString() {
                return null;
            }
        }
    }

    private class TradeControlPanel extends StyledScreenRegion {

        private final TiledBackground background;

        private TradeControlPanel() {
            super(2);
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
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
