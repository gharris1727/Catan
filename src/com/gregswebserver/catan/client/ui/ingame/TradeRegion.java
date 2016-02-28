package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.ScrollingScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
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
public class TradeRegion extends UIScreenRegion {

    private static final Point elementOffset;
    private static final Point elementSpacing;
    private static final Dimension acceptButtonSize;
    private static final int panelHeight;
    private static final int elementHeight;

    static {
        elementOffset = Client.staticConfig.getPoint("catan.graphics.interface.ingame.trade.element.offset");
        elementSpacing = Client.staticConfig.getPoint("catan.graphics.interface.ingame.trade.element.spacing");
        acceptButtonSize = Client.staticConfig.getDimension("catan.graphics.interface.ingame.trade.accept.size");
        panelHeight = Client.staticConfig.getInt("catan.graphics.interface.ingame.trade.panel.height");
        elementHeight = Client.staticConfig.getInt("catan.graphics.interface.ingame.trade.element.height");
    }

    private final TiledBackground background;
    private final TradeList list;
    private final TradeControlPanel panel;
    private RenderMask tradeSize;
    private Trade selected;

    public TradeRegion(int priority, CatanGame game) {
        super(priority);
        background = new TiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            @Override
            public String toString() {
                return "TradeBackground";
            }
        };
        list = new TradeList(0, game);
        panel = new TradeControlPanel(2);
        add(background).setClickable(this);
        add(list);
        add(panel);
    }

    public void update() {
        list.forceRender();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int scrollHeight = mask.getHeight() - panelHeight;
        RenderMask scrollSize = new RectangularMask(new Dimension(mask.getWidth(), scrollHeight));
        RenderMask panelSize = new RectangularMask(new Dimension(mask.getWidth(), panelHeight));
        background.setMask(scrollSize);
        list.setHostView(scrollSize, new Insets(0,0,0,0));
        panel.setMask(panelSize);
        panel.setPosition(new Point(0, scrollHeight));
        tradeSize = new RectangularMask(new Dimension(mask.getWidth(),elementHeight));
    }

    public String toString() {
        return "TradeScreenArea";
    }

    private class TradeList extends ScrollingScreenRegion {

        private final CatanGame game;

        protected TradeList(int priority, CatanGame game) {
            super(priority);
            this.game = game;
        }

        @Override
        protected void renderContents() {
            clear();
            int height = 1;
            for (Trade t : game.getTrades()) {
                TradeListElement elt = new TradeListElement(0, t);
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

        private class TradeListElement extends UIScreenRegion {

            private final TiledBackground background;
            private final Trade trade;

            protected TradeListElement(int priority, Trade trade) {
                super(priority);
                this.trade = trade;
                background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                    @Override
                    public String toString() {
                        return "TradeListElementBackground";
                    }
                };
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
                selected = trade;
                return null;
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

    private class TradeControlPanel extends UIScreenRegion {

        private final TiledBackground background;
        private final Button acceptTrade;

        protected TradeControlPanel(int priority) {
            super(priority);
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                @Override
                public String toString() {
                    return "TradeControlPanelBackground";
                }
            };
            add(background).setClickable(this);
            acceptTrade = new Button(1, "Accept") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return selected == null ? null : new UserEvent(this, UserEventType.Make_Trade, selected);
                }

                @Override
                public String toString() {
                    return "AcceptTradeButton";
                }
            };
            acceptTrade.setMask(new RoundedRectangularMask(acceptButtonSize));
            add(acceptTrade);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        protected void renderContents() {
            center(acceptTrade);
        }

        @Override
        public String toString() {
            return "TradeControlPanel";
        }
    }
}
