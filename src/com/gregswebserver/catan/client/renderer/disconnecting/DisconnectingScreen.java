package com.gregswebserver.catan.client.renderer.disconnecting;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/17/2015.
 * A screen shown when the client disconnects for whatever reason.
 */
public class DisconnectingScreen extends UIScreenRegion {

    private final int spacing = 16;

    private TiledBackground background;
    private TextLabel text;
    private Button button;

    public DisconnectingScreen(String message) {
        super(0);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            public String toString() {
                return "DisconnectingScreenBackground";
            }
        };
        text = new TextLabel(1, UIStyle.FONT_HEADING, message) {
            public String toString() {
                return "DisconnectingScreenDisconnectReason";
            }
        };
        button = new Button(2, "Return to connect screen.") {
            public String toString() {
                return "DisconnectingScreenDisconnectButton";
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Net_Disconnect, null);
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
        add(button);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        button.setMask(new RoundedRectangularMask(new Dimension(256, 32)));

        Point textPosition = text.getPosition();
        Point textCenter = getCenteredPosition(text.getGraphic().getMask());
        textPosition.x = textCenter.x;
        textPosition.y = textCenter.y - spacing;

        Point buttonPosition = button.getPosition();
        Point buttonCenter = getCenteredPosition(button.getMask());
        buttonPosition.x = buttonCenter.x;
        buttonPosition.y = buttonCenter.y + spacing;
    }

    @Override
    protected void restyleContents(UIStyle style) {
        background.setStyle(style);
        text.setStyle(style);
        button.setStyle(style);
    }

    public String toString() {
        return "DisconnectingScreen";
    }
}
