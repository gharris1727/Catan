package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.ColorScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.GridScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.UIStyle;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.TextGraphic;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends GridScreenRegion {

    private static final int serverWidth = 384;
    private static final int serverHeight = 72;
    private static final RenderMask serverSize = new RoundedRectangularMask(new Dimension(serverWidth, serverHeight));
    private static final int listGap = 4;
    private static final int buttonHeight = 96;
    private static final RenderMask buttonPanelSize = new RoundedRectangularMask(new Dimension(serverWidth, buttonHeight));
    private static final int vertPadding = 48;
    private final UIStyle style;
    private final ServerList list;
    private int scroll;
    private int displayed;
    private int selected;

    public ServerListRegion(Point position, int priority, RenderMask mask, UIStyle style, ServerList list) {
        super(position, priority, mask);
        this.style = style;
        this.list = list;
        scroll = 0;
        displayed = 0;
        selected = -1;
    }

    public void setMask(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        //Find out the horizontal padding on either side.
        int wPadding = width - serverWidth;
        int lPadding = wPadding / 2;
        int rPadding = wPadding / 2 + wPadding % 2;
        //Compile the widths together into an array.
        int[] widths = new int[]{lPadding, serverWidth, rPadding};
        //Find out how many servers we can display.
        displayed = ((height - vertPadding - buttonHeight) / serverHeight) - 1;
        //Create a new dynamic array that contains the list of heights.
        int[] heights = new int[displayed * 2 + 3];
        //Figure out the vertical padding we need
        int vPadding = height - (serverHeight + listGap) * displayed - buttonHeight;
        int uPadding = vPadding / 2;
        int dPadding = vPadding / 2 + vPadding % 2;
        //Fill out the array of heights
        heights[0] = uPadding;
        for (int i = 0; i < displayed; i++) {
            heights[2 * i + 1] = serverHeight;
            heights[2 * i + 2] = listGap;
        }
        heights[heights.length - 2] = buttonHeight;
        heights[heights.length - 1] = dPadding;
        //Finally send all of this to the grid layout.
        setGridSize(widths, heights, mask);
    }

    protected void render() {
        clear();
        for (int i = 0; i < displayed && i + scroll < list.size(); i++)
            add(new ServerListItem(new Point(1, 2 * i + 1), 0, i + scroll, serverSize));
        add(new ButtonPanelAreaScreen(new Point(1, displayed * 2 + 1), 0, buttonPanelSize));
    }

    public String toString() {
        return "ServerListArea";
    }

    private class ServerListItem extends ColorScreenRegion {

        private final int listIndex;

        public ServerListItem(Point position, int priority, int listIndex, RenderMask mask) {
            super(position, priority, mask);
            this.listIndex = listIndex;
        }

        protected void render() {
            clear();

            ConnectionInfo info = list.get(listIndex);
            String address = "Remote Address : " + info.getRemote() + ":" + info.getPort();
            Graphic addressGraphic = new TextGraphic(style.getLightTextStyle(), address);
            String login = "Username: " + info.getUsername();
            Graphic loginGraphic = new TextGraphic(style.getLightTextStyle(), login);

            add(new EdgedTiledBackground(new Point(), 0, getMask(), style.getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            }).setClickable(this);
            add(new StaticObject(new Point(16, 16), 1, addressGraphic) {
                public String toString() {
                    return "ServerListItem Remote Address";
                }
            }).setClickable(this);
            add(new StaticObject(new Point(16, 40), 2, loginGraphic) {
                public String toString() {
                    return "ServerListItem Username";
                }
            }).setClickable(this);
        }

        public UserEvent onMouseClick(MouseEvent event) {
            selected = listIndex;
            return null;
        }

        public String toString() {
            return "ServerListItemArea " + listIndex;
        }
    }

    private class ButtonPanelAreaScreen extends ColorScreenRegion {

        public ButtonPanelAreaScreen(Point position, int priority, RenderMask mask) {
            super(position, priority, mask);
        }

        protected void render() {
            clear();
            add(new EdgedTiledBackground(new Point(), 0, getMask(), style.getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            }).setClickable(this);
            add(new Button(new Point(16, 16), 1, new RoundedRectangularMask(new Dimension(128, 32)), style, "Connect") {
                public String toString() {
                    return "Button Panel";
                }
            });

        }

        public String toString() {
            return "ButtonPanelArea";
        }
    }

}
