package com.gregswebserver.catan.client.renderer.primary.disconnecting;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/17/2015.
 * A screen shown when the client disconnects for whatever reason.
 */
public class DisconnectingScreenRegion extends UIScreenRegion {

    private final int spacing = 16;

    private ScreenRegion background;
    private Graphic textGraphic;
    private ScreenObject text;
    private RenderMask buttonMask;
    private ScreenObject button;

    public DisconnectingScreenRegion(RenderMask mask, UIStyle style, String message) {
        super(new Point(), 0, mask, style);
        background = new EdgedTiledBackground(new Point(), 0, mask, style.getBackgroundStyle()) {
            public String toString() {
                return "DisconnectingScreen Background";
            }
        };
        textGraphic = new TextGraphic(style.getDarkTextStyle(), message);
        text = new StaticObject(new Point(), 1, textGraphic) {
            public String toString() {
                return "Disconnect Reason";
            }
        };
        buttonMask = new RoundedRectangularMask(new Dimension(256, 32));
        button = new Button(new Point(), 2, buttonMask, style, "Return to connect screen.") {
            public String toString() {
                return "Disconnect Button";
            }

            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Net_Disconnect, null);
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
        add(button);
        resizeContents(mask);
    }

    protected void resizeContents(RenderMask mask) {
        background.resize(mask);
        Point textPosition = text.getPosition();
        Point textCenter = getCenteredPosition(textGraphic.getMask());
        textPosition.x = textCenter.x;
        textPosition.y = textCenter.y - spacing;

        Point buttonPosition = button.getPosition();
        Point buttonCenter = getCenteredPosition(buttonMask);
        buttonPosition.x = buttonCenter.x;
        buttonPosition.y = buttonCenter.y + spacing;
    }

    public String toString() {
        return "DisconnectingScreen";
    }
}
